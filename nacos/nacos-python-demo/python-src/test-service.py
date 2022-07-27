
from http import server
from flask import Flask, jsonify, request

server = Flask(__name__)

server.config['JSON_AS_ASCII'] = False

@server.route('/foo', methods = ['get'])
def foo():
    return {
        'name': 'fff',
        'age': 18,
        'hobbies': ['basketball', 'badminton']
    }

@server.route('/bar/<name>', methods = ['get'])
def bar(name):
    return "hello, " + name

server.run(port = 2100)

