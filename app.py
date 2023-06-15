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

def get_random_image(offset, count):
    collection_ref = db.collection('clothing')
    
    total = len(collection_ref.get())

    actual_count = min(total - offset, count)

    random_docs = random.sample(list(collection_ref.get()), actual_count)

    image_data = []
    for doc in random_docs:
        image_url = doc.get('imageUrl')
        string_label = doc.get('string_label')
        integer_label = doc.get('integer_label')
        string_color = doc.get('string_color')
        integer_color = doc.get('integer_color')
        image_data.append({
            'imageUrl': image_url,
            'string_label': string_label,
            'integer_label': integer_label,
            'string_color': string_color,
            'integer_color': integer_color
        })

    return image_data


@app.route('/predict', methods=['POST'])
def predict():
    try:
            
        if 'file' not in request.files:
            return jsonify({'error': 'No file uploaded'})
        label = ['Blazzer', 'Coat', 'Denim_Jacket', 'Dress', 'Gym_Jacket', 'Hoodie', 'Jacket', 'Jeans', 'Pants', 'Polo', 'Shirt (Kemeja)', 'Shorts', 'Skirt', 'Sweter', 'T-Shirt']
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
    

import numpy as np

def generate_combinations(input_array):


    #Parisian
    parisian_tops = ['dress', 'shirt']
    parisian_bottoms = ['pants', 'skirt']
    parisian_outer = ['blazer', 'coat', 'sweater']

    parisian_input_top = []
    parisian_input_bottom = []
    parisian_input_outer = []

    #Athlete
    athelete_tops = ['tshirt']
    athelete_bottoms = ['shorts']
    athelete_outer = ['coat', 'gym_jacket', 'hoodie', 'sweater']

    athelete_input_top = []
    athelete_input_bottom = []
    athelete_input_outer = []

    #Streetwear
    street_tops = ['dress', 'shirt', 'tshirt']
    street_bottoms = ['jeans', 'shorts', 'pants', 'skirt' ]
    street_outer = ['coat', 'denim_jacket', 'gym_jacket', 'hoodie', 'jacket', 'sweater']

    street_input_top = []
    street_input_bottom = []
    street_input_outer = []

    #Business
    business_tops = ['dress', 'shirt']
    business_bottoms = ['pants']
    business_outer = ['blazer', 'sweater']

    business_input_top = []
    business_input_bottom = []
    business_input_outer = []

    #Calm
    calm_tops = ['dress', 'shirt', 'tshirt']
    calm_bottoms = ['jeans', 'shorts', 'pants', 'skirt']
    calm_outer = ['gym_jacket', 'hoodie', 'jacket', 'sweater']

    calm_input_top = []
    calm_input_bottom = []
    calm_input_outer = []

    #Grunge
    grunge_tops = ['dress', 'tshirt']
    grunge_bottoms = ['jeans', 'shorts', 'pants']
    grunge_outer = ['coat', 'denim_jacket', 'jacket']

    grunge_input_top = []
    grunge_input_bottom = []
    grunge_input_outer = []

    for item in input_array:
        id_num, color, clothing_type = item

        if color in ['black', 'white', 'blue']:
            #Parisian
            if clothing_type in parisian_tops:
                parisian_input_top.append([id_num, color, clothing_type])
            
            if clothing_type in parisian_bottoms:
                parisian_input_bottom.append([id_num, color, clothing_type])
            
            if clothing_type in parisian_outer:
                parisian_input_outer.append([id_num, color, clothing_type])

        if color in ['black', 'white', 'red', 'blue']:
            #Athlete
            if clothing_type in athelete_tops:
                athelete_input_top.append([id_num, color, clothing_type])
            
            if clothing_type in athelete_bottoms:
                athelete_input_bottom.append([id_num, color, clothing_type])
            
            if clothing_type in athelete_outer:
                athelete_input_outer.append([id_num, color, clothing_type])
        
        if color in ['black', 'white', 'red', 'orange', 'green', 'blue', 'violet']:
            #Streetwear
            if clothing_type in street_tops:
                street_input_top.append([id_num, color, clothing_type])
            
            if clothing_type in street_bottoms:
                street_input_bottom.append([id_num, color, clothing_type])
            
            if clothing_type in street_outer:
                street_input_outer.append([id_num, color, clothing_type])
        
        if color in ['black', 'white', 'blue']:
            #Business
            if clothing_type in business_tops:
                business_input_top.append([id_num, color, clothing_type])

            if clothing_type in business_bottoms:
                business_input_bottom.append([id_num, color, clothing_type])

            if clothing_type in business_outer:
                business_input_outer.append([id_num, color, clothing_type])

        if color in ['black', 'white', 'red', 'orange', 'yellow', 'green', 'blue', 'violet']:
            #Calm
            if clothing_type in calm_tops:
                calm_input_top.append([id_num, color, clothing_type])
            
            if clothing_type in calm_bottoms:
                calm_input_bottom.append([id_num, color, clothing_type])
            
            if clothing_type in calm_outer:
                calm_input_outer.append([id_num, color, clothing_type])

        if color in ['black', 'white', 'red', 'blue']:
            #Grunge
            if clothing_type in grunge_tops:
                grunge_input_top.append([id_num, color, clothing_type])
            
            if clothing_type in grunge_bottoms:
                grunge_input_bottom.append([id_num, color, clothing_type])
            
            if clothing_type in grunge_outer:
                grunge_input_outer.append([id_num, color, clothing_type])

    ### Creating a list to save the combinations (outfit)

    #Parisian
    parisian_combinations = []
    parisian_combinations_outer = []

    #Athelete
    athelete_combinations = []
    athelete_combinations_outer = []

    #Streetwear
    street_combinations = []
    street_combinations_outer = []

    #Business
    business_combinations = []
    business_combinations_outer = []

    #Calm
    calm_combinations = []
    calm_combinations_outer = []

    #Grunge
    grunge_combinations = []
    grunge_combinations_outer = []

   
    #Parisian
    for top in parisian_input_top:
        for bottom in parisian_input_bottom:
            parisian_combinations.append(top + bottom)

    for comb in parisian_combinations:
        for outer in parisian_input_outer:
            parisian_combinations_outer.append(comb + outer)
    
    #Athelete
    for topa in athelete_input_top:
        for bottoma in athelete_input_bottom:
            athelete_combinations.append(topa + bottoma)

    for comba in athelete_combinations:
        for outera in athelete_input_outer:
            athelete_combinations_outer.append(comba + outera)

    #Streetwear
    for tops in street_input_top:
        for bottoms in street_input_bottom:
            street_combinations.append(tops + bottoms)

    for combs in street_combinations:
        for outers in street_input_outer:
            street_combinations_outer.append(combs + outers)

    #Business
    for topb in business_input_top:
        for bottomb in business_input_bottom:
            business_combinations.append(topb + bottomb)

    for combb in business_combinations:
        for outerb in business_input_outer:
            business_combinations_outer.append(combb + outerb)

    #Calm
    for topc in calm_input_top:
        for bottomc in calm_input_bottom:
            calm_combinations.append(topc + bottomc)

    for combc in calm_combinations:
        for outerc in calm_input_outer:
            calm_combinations_outer.append(combc + outerc)

    #Grunge
    for topg in grunge_input_top:
        for bottomg in grunge_input_bottom:
            grunge_combinations.append(topg + bottomg)

    for combg in grunge_combinations:
        for outerg in grunge_input_outer:
            grunge_combinations_outer.append(combg + outerg)

    result = delete_dupe(parisian_combinations, parisian_combinations_outer, athelete_combinations, athelete_combinations_outer, street_combinations, street_combinations_outer, business_combinations, business_combinations_outer, calm_combinations, calm_combinations_outer, grunge_combinations, grunge_combinations_outer)
    return result

### Deleting duplicate output

def delete_dupe(*arrays): #Deleting duplicate outfit from different categories
    unique_outfit = [] 
    dupe = []

    for arra in arrays:
        # if the input is not in dupe then it will be put into the dupe and then appended to unique_outfit
        if arra not in dupe:
            dupe.append(arra)
            unique_outfit.append(arra)

    return unique_outfit  

@app.route('/recommendation', methods=['POST'])
def recommendation():
    data = request.json
    

    output_combinations = generate_combinations(np.array(data["listCombination"]))
    
    image_data = {
        "combinationId":str(uuid.uuid4()),
        "combinationName":data["name"],
        "images":  output_combinations}


    # Create the response data
    if len(output_combinations) > 1:
        return jsonify( {
            "success": True,
            "message": "Kombinasi Sukses",
            "data": image_data
            })
    else:
        return jsonify( {
            "success": False,
            "message": "Kombinasi Gagal",
            "data": image_data
            })

   

if __name__ == '__main__':
    app.run(debug=True)
    
