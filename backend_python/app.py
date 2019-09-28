from flask import Flask
from flask import request
from flask import json
from gensim.models import KeyedVectors
from analyzer import predict

app = Flask(__name__)

@app.route("/")
def main():
    return ""

@app.route("/getInterest", methods=['POST'])
def getInterest():
    req_data = request.get_json()
    interests = predict(model, req_data['groups'], final_numb_topic=5,
                        init_numb_top=5, numb_syn=10)

    return json.dumps(interests, ensure_ascii=False).encode('utf8')


if __name__ == "__main__":
    model = KeyedVectors.load_word2vec_format('ruwiki_20180420_100d.txt')
    app.run(debug=True, host="0.0.0.0", port=81, use_reloader=False)
