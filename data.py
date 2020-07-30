from typing import List
import random
import time

def init_db():
    import pyrebase
    config = {
        "apiKey": "bccdb0a053ff6210f4944c55f87fc6d241e199b0",
        "authDomain": "data-5eef0.firebaseapp.com",
        "databaseURL": "https://data-5eef0.firebaseio.com/",
        "storageBucket": "data-5eef0.appspot.com",
    }

    firebase = pyrebase.initialize_app(config)
    db = firebase.database()
    return db


class DataBase:
    def __init__(self, teacher_name: str, studentList: List) -> None:
        self.studentList = studentList
        self.teacher_name = teacher_name
        self.db = init_db()
        self.db.child("nameList").child("teacher_name").push(self.teacher_name)
        for stu in self.studentList:
            self.db.child("nameList").child("student_name").push(stu)
        self.db.child("online").push(True)

    def insert_data(self, name, score):
        self.db.child(self.teacher_name).child(name).push(score)

    def end_ses(self):
    time.sleep(2)
        self.db.child("online").push(False)


student_list = ["surbhi", "saumya", "kanishka", "praneeth", "ayush"]
my_data = DataBase("Ritik The Great", student_list)

for i in range(500):
    for s in student_list:
        score = random.randrange(0,100)
        my_data.insert_data(s, i+100)
    time.sleep(10)
