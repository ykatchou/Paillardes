#!/usr/bin/python

from pysqlite2 import dbapi2 as sqlite


####
#### D A T A   S T R U C T U R E S
######################################################

#Gros conteneur !
class DB:
    def __init__(self):
        self.Chanson = set()
        self.Filliere = set()
        self.Tag = set()
        self.ChansonFilliere = set()
        self.ChansonTag = set()


class Chanson:
    def __init__(self, id=0, titre="", paroles="",url="",midi=""):
        self.id=id
        self.titre=titre
        self.paroles=paroles
        self.url=url
        self.midi=midi

    def tostring(self):
        return(self.id + '\t'+self.titre)


class Filliere:
    def __init__(self, id=0, nom=""):
        self.id=id
        self.nom=nom
    def tostring(self):
        return(self.id + '\t'+self.nom)


class Tag:
    def __init__(self, id=0, value=""):
        self.id=id
        self.value=value
    def tostring(self):
        return(self.id + '\t'+self.value)


class ChansonFilliere:
    def __init__(self, id=0, chanson_id=0, filliere_id=0):
        self.id=id
        self.chanson_id=chanson_id
        self.filliere_id=filliere_id

    def tostring(self):
        return('Chanson ' + self.chanson_id + ' - ' + self.filliere_id)

class ChansonTag:
    def __init__(self,id=0,chanson_id=0,tag_id=0):
        self.id=id
        self.chanson_id=chanson_id
        self.tag_id=tag_id

    def tostring(self):
        return('Chanson ' + self.chanson_id + ' + ' + self.tag_id)


####
#### D A T A   A C C E S S
######################################################

class DatabaseLoader:
    def __init__(self):
        self.connection = sqlite.connect('paillardes.db')
        self.memoryConnection = sqlite.connect(':memory:')

    def loadingChansons(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Titre,Paroles,Url,Midi FROM CHANSON')
        for row in cursor:
            db.Chanson.add(Chanson(row[0],row[1],row[2],row[3],row[4]))
        cursor.close()

    def loadingFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Nom FROM Filliere')
        for row in cursor:
            db.Filliere.add(Filliere(row[0],row[1]))
        cursor.close()

    def loadingTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Value FROM Tag')
        for row in cursor:
            db.Tag.add(Tag(row[0],row[1]))
        cursor.close()

    def loadingChansonFilliere(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, filliere_id FROM ChansonFilliere')
        for row in cursor:
            db.ChansonFilliere.add(ChansonFilliere(row[0],row[1],row[2]))
        cursor.close()

    def loadingChansonTag(self, db):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id, chanson_id, tag_id FROM ChansonTag')
        for row in cursor:
            db.ChansonTag.add(ChansonTag(row[0],row[1],row[2]))
        cursor.close()

    def __del__(self):
        self.connection.close()

####
#### D A T A   F U N C T I O N S
######################################################



####
#### M A I N
######################################################

class Main:
    def main(self):
        db = DB()

        dbload = DatabaseLoader()
        dbload.loadingChansons(db)
        dbload.loadingFilliere(db)
        dbload.loadingTag(db)
        dbload.loadingChansonFilliere(db)
        dbload.loadingChansonTag(db)

        print'done.'

####
#### I N I T I A L   L A U N C H E R
######################################################
m = Main()
m.main()



