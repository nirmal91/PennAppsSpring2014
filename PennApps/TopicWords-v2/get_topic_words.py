import os

def get_topic_words(directory):
	
	folders = os.listdir(directory)

	for f in folders:
		print f

        	lstring = """==== Do not change these values ====
stopFilePath = stoplist-smart-sys.txt
performStemming = N
backgroundCorpusFreqCounts = bgCounts-Giga.txt
topicWordCutoff = 0.1

==== Directory to compute topic words on ====
inputDir = """
		
		lstring += directory + "/" + f

        	lstring += "/\n"

        	lstring += """==== Output File ====
outputFile = /home1/b/bhasink/cis530/Summarizer/topic_words/"""
        	lstring += f + ".ts"
        	out = open("/home1/b/bhasink/cis530/Summarizer/TopicWords-v2/config.example", "w")
       		out.write(lstring)
       		out.flush()
       		out.close()

		os.system('java -Xmx1000m TopicSignatures config.example')

get_topic_words('/home1/c/cis530/final_project/test_input')
