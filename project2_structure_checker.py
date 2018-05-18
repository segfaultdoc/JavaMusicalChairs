######################################################
# CS 463: Project 2 Checker
# Created by Julian Easley
# Uses Python 3
######################################################
import re
import sys

filename = sys.argv[1] # grabs the 1st argument as the filename

num_lines = sum(1 for line in open(filename)) # number of lines in the file
num_players = 0 # total number of players
num_players_left = 0 # number of players left after each round
player_list = [] # list of all the players still in the game
players_not_sitted = [] # list of all the players not sitted yet
chair_list = [] # list of all the chairs available
num_errors = 0 # number of errors

#------------- OPENING THE FILE -------------#
with open(filename) as fp:
	
	line = fp.readline()
	i = 1 # line number
	j = 0 # round line number
	round_count = 1 # round number
	words = [] # words on a specific line
	
	#------------------ WHILE ------------------#
	while line:
		# checks if its the beginning of the file
		if(i == 1): 
			words = line.strip().split() 
			# check that it starts with "begin" keyword
			if(words[0].lower() != "begin"): 
				print("Line 1: Missing BEGIN keyword at the beginning")
				num_errors += 1
				break
			
			# checks that if the student only wrote one word or all three
			if(len(words) != 3 and len(words) == 1): 
				print("Line 1: The number of player and the keyword \"players\" needs to be printed.")
				num_errors += 1
				break
			
			# checks that the student wrote the number of players
			if(words[1].isdigit()):
				num_players = int(words[1])
				num_players_left = num_players
				player_list = [i+1 for i in range(num_players)] # initializes all players in the list
				
				# checks if the there is a word after the number
				if(len(words) != 3 and len(words) == 2):
					print("Line 1: After BEGIN and the number of players, the \"players\" keyword needs to be printed.")
					num_errors += 1
				
				# checks if the word after the number is the keyword "players"
				elif((words[2].lower() != "players".lower()) and (words[2].lower() != "player".lower())):
					print("Line 1: After BEGIN and the number of players, the keyword after the number should be \"players\" not \""+words[2]+"\".")
					num_errors += 1
			
			# the student wrote only "begin" at the beginning of the file
			else:
				print("Line 1: After BEGIN, the number of players needs to be stated.")
				num_errors += 1
				break
		
		# checks that the end of the file has the keyword "end"						
		elif(i == num_lines):
			if(line.strip().lower() != "end"):
				print("Line "+str(i)+": Missing END keyword at the ending")
				num_errors += 1
		
		# checks that we are in a round
		elif(line.strip() != "" and num_players != round_count):
			# checks that first line of the round is "round #"
			if(j == 0):
				players_not_sitted = player_list[:]
				# print(str(players_not_sitted)+" <-- BEGIN ") # Debugger statement
				chair_list = [i+1 for i in range(num_players_left-1)] 
				words = line.strip().split() 
				
				m = re.match(r"P(\d+) wins!",line.strip())
				# checks that the game does not end too soon 
				if(m):
					print("Line "+str(i)+": Game ended too early. There are more rounds available.")
					num_errors += 1
					break
				
				# checks that the beginning of a round starts with the "round" keyword
				if(words[0].lower() != "round"):
					print("Line "+str(i)+": In the beginning of each round, \"round #\" needs to be stated.")
					num_errors += 1
				
				# checks that after the keyword "round" there needs to be the round number
				if(len(words) == 1 or not(words[1].isdigit())):
					print("Line "+str(i)+": After the \"round\" keyword, there needs to be the round number.")
					num_errors += 1
				
				# checks that the round number is correct
				if(len(words) == 2 and int(words[1]) != round_count):
					print("Line "+str(i)+": Incorrect round count!")
					num_errors += 1
			
			# checks that the second line of the round is "music off"		
			elif(j == 1):
				if(line.strip().lower() != "music off"):
					print("Line "+str(i)+": The keywords \"music off\" needs to be printed on the next line after the round number.")
					num_errors += 1
					break
			
			# checks that the last line of the round, number of chairs (number of players left) + line (round #) + line (music off))	
			elif(j == (num_players_left-1)+2): 
				# print(str(players_not_sitted)+" <-- END ") # Debugger statement
				m = re.match(r"P(\d+) lost",line.strip())
				# checks that it matches regular expression format
				if(m): 
					# checks the player lost was originally in the player list
					if(int(m.group(1)) not in player_list):
						print("Line "+str(i)+": Player "+m.group(1)+" was removed from the game earlier or never existed.")
						num_errors += 1
					else:
						player_list.remove(int(m.group(1))) # remove player from list
						num_players_left -= 1
				else:
					print("Line "+str(i)+": Wrong format! Should be \"P# lost\".")
					num_errors += 1
				j = -1 # reset counter for next round
				round_count += 1
			
			# line where it prints that the player sits in a specific chair
			else:
				# print(players_not_sitted) # Debugger statement
				m = re.match(r"P(\d+) sat in C(\d+)",line.strip())
				# checks that it matches regular expression format
				if(m): 
					# check if player exist in the player list
					if(int(m.group(1)) not in player_list):
						print("Line "+str(i)+": Player "+m.group(1)+" was removed from the game earlier or never existed.")
						num_errors += 1
						
					# checks if the player is already sitted
					elif(int(m.group(1)) not in players_not_sitted):
						print("Line "+str(i)+": Player "+m.group(1)+" is aready sitted and can't sit in more than one chair.")
						num_errors += 1
					else:
						players_not_sitted.remove(int(m.group(1))) # remove player from players not sitted list
										
					# checks that the chair exists
					if(int(m.group(2)) not in range(1,num_players_left)):
						print("Line "+str(i)+": Chair "+m.group(2)+" does not exist.")
						num_errors += 1
					
					# chair is not available
					elif(int(m.group(2)) not in chair_list):
						print("Line "+str(i)+": Chair "+m.group(2)+" is not available.")
						num_errors += 1
					
					# chair is available
					else:
						chair_list.remove(int(m.group(2))) # remove chair from availability list
										
				# checks if the round ended too soon
				elif(re.match(r"P(\d+) lost",line.strip())):
					print("Line "+str(i)+": There is still a chair or chairs available: "+str(chair_list)+". There are also players not sitted: "+str(player_list))
					num_errors += 1
					break	
				else:
					print("Line "+str(i)+": Wrong format! Should be: \"P# sat in C#.\"")
					num_errors += 1
			
			# Debugger statement		
			#if(j == 0):
			#	print("\tRound "+str(round_count)+": "+str(player_list))
				
			j += 1
		
		# checks that the last round was completed
		elif(line.strip() != "" and num_players == round_count):
			
			# Debugger statement
			#if(j == 0):
			#	print("\tRound "+str(round_count)+": "+str(player_list))
				
			m = re.match(r"P(\d+) wins!",line.strip())
			# checks that it matches regular expression format
			if(m):
				# check if the player number is correct
				if(int(m.group(1)) not in player_list):
					print("Line "+str(i)+": Player "+m.group(1)+" was removed from the game earlier or never existed.")
					num_errors += 1
			else:
				print("Line "+str(i)+": Wrong format! Should be \"P# wins!\".")
				num_errors += 1
			
		line = fp.readline()
		i += 1
	#------------------ END OF WHILE ------------------#

if(num_errors == 0):
	print("Everything looks good!")