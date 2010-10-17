#!/usr/bin/python

####
#### D A T A   S T R U C T U R E S
######################################################

#Gros conteneur !
class DB:
    def __init__(self):
        self.Chanson = list()
        self.Filliere = list()
        self.Tag = list()
        self.ChansonFilliere = list()
        self.ChansonTag = list()

class Chanson:
    def __init__(self, id=0, titre="", paroles="",url="",midi=""):
        self.id=id
        self.titre=titre
        self.paroles=paroles
        self.url=url
        self.midi=midi

    def tostring(self):
        return(str(self.id) + '\t'+self.titre)

    def printData(self, onlyInfos):
        print self.tostring()
        if self.url: 
            print'URL :\t' + self.url
        if self.midi:
            print'MIDI:\t' + self.midi
        if not onlyInfos:
            print'----------------------------------------------'
            print'PAROLES:\t'
            print'----------------------------------------------'
            print self.paroles

class Filliere:
    def __init__(self, id=0, nom=""):
        self.id=id
        self.nom=nom
    def tostring(self):
        return(str(self.id) + '\t'+self.nom)


class Tag:
    def __init__(self, id=0, value=""):
        self.id=id
        self.value=value
    def tostring(self):
        return(str(self.id) + '\t'+self.value)


class ChansonFilliere:
    def __init__(self, id=0, chanson_id=0, filliere_id=0):
        self.id=id
        self.chanson_id=chanson_id
        self.filliere_id=filliere_id
    def tostring(self):
        return('Chanson ' + str(self.chanson_id) + ' - ' + str(self.filliere_id))

class ChansonTag:
    def __init__(self,id=0,chanson_id=0,tag_id=0):
        self.id=id
        self.chanson_id=chanson_id
        self.tag_id=tag_id
    def tostring(self):
        return('Chanson ' + str(self.chanson_id) + ' + ' + str(self.tag_id))

