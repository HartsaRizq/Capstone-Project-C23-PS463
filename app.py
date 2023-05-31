import os
import numpy as np
import tensorflow as tf
from flask import Flask, request, jsonify
from tensorflow.keras.preprocessing import image
import tensorflow_hub as hub
from PIL import Image
import io
import cv2
import uuid
import datetime
import random

import firebase_admin
from firebase_admin import credentials, firestore, storage

cred = credentials.Certificate("wearitsuw.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

my_model = tf.keras.models.load_model('clothing-recognition.h5', custom_objects={'KerasLayer': hub.KerasLayer})


app = Flask(__name__)

def upload_image_to_storage(image, filename):
    bucket = storage.bucket('wearitsuw.appspot.com')
    blob = bucket.blob(filename)

   
    blob.content_type = 'image/jpeg' 

    image.seek(0)

    blob.upload_from_file(image, content_type='image/jpeg')
    blob.make_public()

    return blob.public_url

def get_random_image(num_images):

    collection_ref = db.collection('clothing')

    docs = collection_ref.get()
    random_docs = random.sample(docs, num_images)

    image_urls = [doc.get('imageUrl') for doc in random_docs]

    return image_urls


@app.route('/predict', methods=['POST'])
def predict():
    try:
            
        if 'file' not in request.files:
            return jsonify({'error': 'No file uploaded'})
        label = ['Coat', 'Sweter', 'Skirt', 'Polo', 'T-Shirt', 'Shorts', 'Hoodie', 'Jacket', 'Shirt (Kemeja)', 'Dress', 'Denim_Jacket', 'Pants', 'Jeans', 'Gym_Jacket', 'Blazzer']
        label_mapping = {
            'Blazer': 11,
            'Coat': 12,
            'Denim_Jacket': 13,
            'Dress': 14,
            'Gym_Jacket': 15,
            'Hoodie': 16,
            'Jacket': 17,
            'Jeans': 18,
            'Shorts': 19,
            'Pants': 20,
            'Shirt': 21,
            'Skirt': 22,
            'Sweater': 23,
            'T-Shirt': 24
        }

        color_mapping = {
        'Black': 0,
        'White': 255,
        'Red': 178,
        'Orange': 69,
        'Yellow': 510,
        'Green': 250,
        'Blue': 205,
        'Violet': 127
        }


        filenya = request.files['file']
        img = Image.open(io.BytesIO(filenya.read()))
        img = img.resize((224, 224))
        x = image.img_to_array(img)
        x /= 255
        x = np.expand_dims(x, axis=0)
        images = np.vstack([x])

        pred_arr = my_model.predict(images, batch_size=5)

        predict = np.argmax(pred_arr, axis=1)

        prediction = label[predict[0]]

        img_cv2 = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)
        height, width, _ = img_cv2.shape
        x = width // 2
        y = height // 2
        color_code = img_cv2[y, x]

        red, green, blue = color_code[::-1]

        if red < 50 and green < 50 and blue < 50:
            color = "Black"
        elif red > 200 and green > 200 and blue > 200:
            color = "White"
        else:
            hsv = cv2.cvtColor(img_cv2, cv2.COLOR_BGR2HSV)
            hue_value = hsv[y, x, 0]

            color = "Undefined"
            if hue_value < 5 or hue_value > 170:
                color = "Red"
            elif 5 <= hue_value < 22:
                color = "Orange"
            elif 22 <= hue_value < 33:
                color = "Yellow"
            elif 33 <= hue_value < 78:
                color = "Green"
            elif 78 <= hue_value < 131:
                color = "Blue"
            elif 131 <= hue_value < 170:
                color = "Violet"

        unique_id = str(uuid.uuid4())
        unique_filename = str(uuid.uuid4()) + '.jpg'

        
        
        image_url = upload_image_to_storage(filenya, unique_filename)


        response = {
        'string_label': prediction,
        'imageId': unique_id,
        'integer_label': label_mapping[prediction],
        'string_color': color,
        'integer_color': color_mapping[color],
        'imageUrl': image_url   
        }
        doc_ref = db.collection('clothing').document(unique_id)
        doc_ref.set(response)

        return jsonify(response)
    except:
        return "Invalid Image"
    

@app.route('/recommendation', methods=['GET'])
def recommendation():
    num_images = 3
    image_urls = get_random_image(num_images)

    return jsonify({'image_urls': image_urls})


if __name__ == '__main__':
    app.run(debug=True)
    