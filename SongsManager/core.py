#!/usr/bin/python

from objects import *
from database import *

####
#### D A T A   F U N C T I O N S
######################################################

#Class Parser : Parcours rapide
class Parser:
    def __init__(self, db):
        self.db = db

    def getChanson(self, chanson_id):
        for c in self.db.Chanson:
            if c.id == chanson_id:
                return c
        return int(chanson_id)

    def getFilliere(self, filliere_id):
        for f in self.db.Filliere:
            if f.id == filliere_id:
                return f
        return int(filliere_id)

    def getTag(self, tag_id):
        for t in self.db.Tag:
            if t.id == tag_id:
                return t
        return int(tag_id)

#Class Render : Mise en forme / jointure -> affichage
class Render:
    def __init__(self, db):
        self.db = db

    def getListTag(self, song_id):
        l = list()
        for ct in self.db.ChansonTag:
            if ct.chanson_id == song_id:
                l.append(int(ct.tag_id))
        return l

    def getListTagString(self, song_id):
        l = list()
        p = Parser(self.db)
        for s in self.getListTag(song_id):
            l.append(p.getTag(s).value)
        return l

    def getListFilliere(self, song_id):
        l = list()
        for cf in self.db.ChansonFilliere:
            if cf.chanson_id == song_id:
                l.append(int(cf.filliere_id))
        return l

    def getListFilliereString(self, song_id):
        l = list()
        p = Parser(self.db)
        for s in self.getListFilliere(song_id):
            l.append(p.getFilliere(s).nom)
        return l

#Class Trigger : Mets a jour les tables N-N 

#Class Helper : Fonctions haut niveaux
