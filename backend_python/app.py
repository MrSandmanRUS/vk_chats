from flask import Flask
from flask import request
from flask import json

app = Flask(__name__)

@app.route("/")
def main():
    return ""

@app.route("/getInterest", methods=['POST'])
def getInterest():
    req_data = request.get_json()
    return json.dumps(["Interest 1", "Interest 2", "Interest 3"])


if __name__ == "__main__":

    app.run(debug=True, host="0.0.0.0", port=81, use_reloader=False)
