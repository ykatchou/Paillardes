#!/usr/bin/python

import sys

from objects import *
from core import *

####
#### I N T E R F A C E   U T I L I S A T E U R
####            M O D E   T E X T E
#################################################

class Menu:
    def __init__(self, db):
        self.isSaved = 1
        self.db = db
        self.separator = '----------------------------------------------'

    def read(self, prompt):
        return raw_input(prompt)
    
    def start(self):
        self.list_root()

    def list_root(self):
        again = 1

        print ''
        print self.separator
        print 'Root menu'
        print self.separator
        print ''
        self.list_command()

        while(again):
            print self.separator
            choice = self.read('>')
            print ''

            if(choice == 'ls'):
                self.list_songs()
            elif(choice == 'lf'):
                self.list_filliere()
            elif(choice == 'lt'):
                self.list_tag()
            elif(choice == 'h' or choice == 'help'):
                self.help()
            elif(choice =='q' or choice == 'exit' or choice == 'bye' or choice == 'quit'):
                again = 0
                self.exit()
            elif again == 1:
                print 'Error : Unknown command'
                print self.separator
                self.list_command()

        return
        
    def list_songs(self):
        print 'Song list'
        for c in self.db.Chanson:
            print c.tostring()

    def list_filliere(self):
        print 'Filliere list'
        for f in self.db.Filliere:
            print f.tostring()

    def list_tag(self):
        print 'Tag list'
        for t in self.db.Tag:
            print t.tostring()

    def list_command(self):
        print 'ls\t: list songs'
        print 'lf\t: list filliere'
        print 'lt\t: list tags'
        print 'help\t: help'
        print 'bye\t: quit'

    def help(self):
        print 'La liste des commandes disponibles :'
        self.list_command()

    def exit(self):
        if not self.isSaved :
            if self.read(not 'Voulez-vous sauvegarder ? [Y/n]') == 'n':
                print 'Sauvegarde !'
            else:
                print 'Pas de sauvegarde'
        return



