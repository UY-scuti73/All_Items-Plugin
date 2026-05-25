InFile = open("Files/input.txt")
OutFile = open("Files/output.txt", 'w')
lines = InFile.readlines()
lines = ["minecraft:" + line for line in lines]
lines = "".join(lines)
OutFile.write(lines)
InFile.close()
OutFile.close()