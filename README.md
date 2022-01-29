# SubstringProblem
This java program is to find longest common substrings between two or more files.
It makes use of the data structure Suffix Tree; however, it's construction is currently O(N^2) in the worst case.
The construction algorithm makes use of the approach of Ukkonen's algorithm, but does not fully implement it.
Currently to optimize dealing with large files it breakes down the text into subparts of M (currently 125) chars long texts.
This way the construction of the Suffix Tree does not grow unboundedly large both for space and time.
Yet it requires for a further check at common substrings with length > M/2 characters, see why at Observations.

TO DO:
Implement the tracking and checking of longest common substrings between files (right now does not check if from different files)
Add a further check for common substrings with length > M/2 characters. Do this by extending the substring to the left and right
until it no longer matches the substring in other indicated file(s).



Observations:
1- To limit time and space for this algorithm the text is broken into M characters long subparts then fed into the the suffix tree.
   Although not solving the problem in linear time this battles the O(N^2) run time.
2- Since the text is broken into M chars long subparts we have to do a further check on common substrings with length > M/2
   this arises from the fact that either the longest substring did not fit in M characters or the longest substring was cut
   into subparts because we broke down the file into M character strings. However it is guranteed that in the worst case we have 
   to look at length > M/2 substrings since it could not have been broken down further than by a factor of 2.
   
Testing:
Currently to test this program, update the txt files under the directory files and line 13 in main to the absolute path of where
the program is located. Note that in the current state 5 files with 4000 characters each (20 000 characters in total) takes around
2 minutes to run, though it may depend on the machine run on.
