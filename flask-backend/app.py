from flask import Flask
from controllers.LanguageProcessAPI import language_process_bp


app = Flask(__name__)
app.register_blueprint(language_process_bp)

if __name__ == "__main__":
    app.run()