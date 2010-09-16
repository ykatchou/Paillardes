#!/usr/bin/python

from pysqlite2 import dbapi2 as sqlite


####
#### D A T A   S T R U C T U R E S
######################################################

class Chanson:
    def __init__(self, id=0, titre="", paroles="",url="",midi=""):
        self.id=id
        self.titre=titre
        self.paroles=paroles
        self.url=url
        self.midi=midi

    def tostring(self):
        return(self.id + '\t'+self.titre)


####
#### D A T A   A C C E S S
######################################################

class DatabaseLoader:
    def __init__(self):
        self.connection = sqlite.connect('paillardes.db')
        self.memoryConnection = sqlite.connect(':memory:')

    def loadingChansons(self, listChanson):
        cursor = self.connection.cursor()
        cursor.execute('SELECT Id,Titre,Paroles,Url,Midi FROM CHANSON')

        for row in cursor:
            c = Chanson(row[0],row[1],row[2],row[3],row[4])
            listChanson.add(c)

    def __del__(self):
        self. connection.close()


####
#### D A T A   F U N C T I O N S
######################################################



####
#### M A I N
######################################################

class Main:
    def main(self):
        listChanson = set()

        dbload = DatabaseLoader()
        dbload.loadingChansons(listChanson)

####
#### I N I T I A L   L A U N C H E R
######################################################
m = Main()
m.main()



