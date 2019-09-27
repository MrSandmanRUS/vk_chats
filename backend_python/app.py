from flask import Flask

app = Flask(__name__)

@app.route("/")
def main():
    return ""

@app.route("/getInterest")
def getInterest():
    return "some interest"


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=81, use_reloader=False)
