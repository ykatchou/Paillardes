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
        self.render = Render(db)
        self.parser = Parser(db)

    def read(self, prompt):
        return input(prompt)

    def start(self):
        self.list_root()

    #### ROOT MODE / SEE 
    ################################################
    def list_root(self):
        again = 1

        print('')
        print(self.separator)
        print('Root menu')
        print(self.separator)
        print('')
        self.list_command()

        while (again):
            print(self.separator)
            choice = self.read('>')
            print('')

            if (choice == 'ls'):
                self.list_songs()
            elif (choice == 'lf'):
                self.list_filliere()
            elif (choice == 'lt'):
                self.list_tag()
            elif (choice[0:2] == 's '):
                if (len(choice[2:]) > 0):
                    song_id = int(choice[2:])
                    self.show_song(song_id, False)
                else:
                    print('Error usage : s <number>')
            elif (choice[0:2] == 'si'):
                if (len(choice[2:]) > 0):
                    song_id = int(choice[2:])
                    self.show_song(song_id, True)
            elif (choice == 'add'):
                self.add_mode()
            elif (choice[0:2] == 'ed'):
                if (len(choice[2:]) > 0):
                    self.edit_mode()
                else:
                    print('Error usage : ed <number>')
            elif (choice == 'h' or choice == 'help'):
                self.help()
            elif (choice == 'q' or choice == 'exit' or choice == 'bye' or choice == 'quit'):
                again = 0
                self.exit()
            else:
                print('Error : Unknown command')
                print(self.separator)
                self.list_command()

            # affiche ou l'on se trouve.
            print('')
            print(self.separator)
            print('Root menu')
        return

    def list_songs(self):
        print('Song list')
        for c in self.db.Chanson:
            print(c.tostring())

    def list_filliere(self):
        print('Filliere list')
        for f in self.db.Filliere:
            print(f.tostring())

    def list_tag(self):
        print('Tag list')
        for t in self.db.Tag:
            print(t.tostring())

    def show_song(self, song_id, onlyInfos):
        for c in self.db.Chanson:
            if c.id == song_id:
                c.printData(onlyInfos)
                print(self.separator)
                print("TAGS      : " + self.render.printListTagString(song_id))
                print("FILLIERES : " + self.render.printListFilliereString(song_id))

    def list_command(self):
        print('ls\t: list songs')
        print('lf\t: list filliere')
        print('lt\t: list tags')
        print('')
        print('s <n>\t: show song')
        print('si <n>\t: show song infos')
        print('')
        print('add\t: add a new song')
        print('ed <n>\t: edit song')
        print('')
        print('help\t: help')
        print('bye\t: quit')

    #### ADD MODE
    ################################################
    def add_mode(self):
        again = 1

        print('')
        print(self.separator)
        print('Add menu')
        print(self.separator)
        print('')
        self.list_command_add()
        while (again):
            print(self.separator)
            choice = self.read('>')
            print('')
            if (choice == 'back' or choice == 'b'):
                again = 0
                return
            elif (choice == 'q' or choice == 'quit' or choice == 'exit'):
                self.exit()
            else:
                print('No available yet !')

            # Affiche ou l'on se trouve.
            print('')
            print(self.separator)
            print('Add menu')

    def list_command_add(self):
        print('at\t: add Title')
        print('au\t: add Url')
        print('am\t: add MIDI')
        print('al\t: add Lyrics')
        print('at\t: add Tags')
        print('af\t: add Filliere')
        print('go\t: commit changes')
        print('back\t: go back to root menu')

    #### EDIT MODE
    ################################################
    def edit_mode(self):
        again = 1

        print('')
        print(self.separator)
        print('Edit menu')
        print(self.separator)
        self.list_command_edit()
        while (again):
            choice = self.read('>')
            print('')
            if (choice == 'cancel'):
                again = 0
                return
            elif (choice == 'q' or choice == 'quit' or choice == 'exit'):
                self.exit()
            else:
                print('Error : Not yet available')

            # Affiche ou l'on se trouve.
            print('')
            print(self.separator)
            print('Edit menu')

    def list_command_edit(self):
        print('save\t: save changes')
        print('delete\t: delete the song')
        print('cancel\t: cancel changes')

    #### GLOBAL 
    ################################################
    def help(self):
        print('La liste des commandes disponibles :')
        self.list_command()

    def exit(self):
        if not self.isSaved:
            if self.read(not 'Voulez-vous sauvegarder ? [Y/n]') == 'n':
                print('Sauvegarde !')
            else:
                print('Pas de sauvegarde')
        return
