#!/usr/bin/python

from pysqlite2 import dbapi2 as sqlite
from objects import *

####
#### D A T A   A C C E S S
######################################################

class DatabaseLoader:
    def __init__(self):
        self.connection = sqlite.connect('paillardes.db')
        self.memoryConnection = sqlite.connect(':memory:')

    def loadingChansons(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Titre,Paroles,Url,Midi FROM CHANSON order by Titre')
        for row in cursor:
            db.Chanson.add(Chanson(row[0],row[1],row[2],row[3],row[4]))
        cursor.close()

    def loadingFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Nom FROM Filliere order by Nom')
        for row in cursor:
            db.Filliere.add(Filliere(row[0],row[1]))
        cursor.close()

    def loadingTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Value FROM Tag order by Value')
        for row in cursor:
            db.Tag.add(Tag(row[0],row[1]))
        cursor.close()

    def loadingChansonFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, filliere_id FROM ChansonFilliere order by chanson_id, filliere_id')
        for row in cursor:
            db.ChansonFilliere.add(ChansonFilliere(row[0],row[1],row[2]))
        cursor.close()

    def loadingChansonTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, tag_id FROM ChansonTag order by chanson_id, tag_id')
        for row in cursor:
            db.ChansonTag.add(ChansonTag(row[0],row[1],row[2]))
        cursor.close()

    def load(self, db):
        self.loadingChansons(db)
        self.loadingFilliere(db)
        self.loadingTag(db)
        self.loadingChansonFilliere(db)
        self.loadingChansonTag(db)

    def __del__(self):
        self.connection.close()


