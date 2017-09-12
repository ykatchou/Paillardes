#!/usr/bin/python

# from sqlite3 import dbapi2 as sqlite
import sqlite3 as sqlite
from objects import *


####
#### D A T A   A C C E S S
######################################################

class DatabaseLoader:
    def __init__(self):
        self.connection = sqlite.connect('paillardes.db')
        # self.memoryConnection = sqlite.connect(':memory:')

    def loadingChansons(self, db):
        tcursor = self.connection.cursor()
        tcursor.execute('SELECT Id,Titre,Paroles,Url,Midi FROM CHANSON order by Titre')
        for row in tcursor:
            db.Chanson.append(Chanson(row[0], row[1], row[2], row[3], row[4]))
        tcursor.close()

    def loadingFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Nom FROM Filliere order by Nom')
        for row in cursor:
            db.Filliere.append(Filliere(row[0], row[1]))
        cursor.close()

    def loadingTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Value FROM Tag order by Value')
        for row in cursor:
            db.Tag.append(Tag(row[0], row[1]))
        cursor.close()

    def loadingChansonFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, filliere_id FROM ChansonFilliere order by chanson_id, filliere_id')
        for row in cursor:
            db.ChansonFilliere.append(ChansonFilliere(row[0], row[1], row[2]))
        cursor.close()

    def loadingChansonTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, tag_id FROM ChansonTag order by chanson_id, tag_id')
        for row in cursor:
            db.ChansonTag.append(ChansonTag(row[0], row[1], row[2]))
        cursor.close()

    def load(self, db):
        self.loadingChansons(db)
        self.loadingFilliere(db)
        self.loadingTag(db)
        self.loadingChansonFilliere(db)
        self.loadingChansonTag(db)

    def __del__(self):
        self.connection.close()
