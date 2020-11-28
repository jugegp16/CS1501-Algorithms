import java.util.*;
/** A class that provides a sorted array-based implementation of 
 * DictInterface. You may use either or both versions of the searchPrefix method, 
 * depending upon how you design your algorithm.  Read over the code and make
 * sure you understand how it works and why it is not very efficient.

 * Adapted from Dr. John Ramirez's CS 1501 Assignment 1
 */
public class MyDictionary implements DictInterface
{
	// Store Strings in an ArrayList
	private ArrayList<String> list;

	public MyDictionary()
	{
		list = new ArrayList<String>();
	}

	// Add new String to end of list.  If String should come before
	// previous last string (i.e. it is out of order) sort the list.
	// We are keeping the data sorted in this implementation of
	// DictInterface to make searches a bit faster.
	public boolean add(String s)
	{
		boolean ans = list.add(s);
		if (list.size() > 1)
		{
			String prevLast = list.get(list.size()-2);
			if (s.compareTo(prevLast) < 0)
				Collections.sort(list);
		}
		return ans;
	}

	// Implement the searchPrefix method as described in the
	// DictInterface class.
	public int searchPrefix(StringBuilder s)
	{
		return searchPrefix(s, 0, s.length()-1);
	}

	public int searchPrefix(StringBuilder s, int start, int end)
	{
		int status = 0;
		boolean doneIn, doneOut, currTest, prefix, word;
		String curr;
		doneOut = false;
		prefix = false;
		word = false;

		// Iterate through the ArrayList, until the end or until the
		// key is passed up (since it is sorted).
		for (int i = 0; i < list.size() && !doneOut; i++)
		{
			doneIn = false;
			curr = list.get(i);
			// Iterate through the key and the current String in the
			// ArrayList character by character.  
			for (int j = start; j <= end && !doneIn; j++)
			{
				int jj = j - start;
				// We have  gone past the end of the string in the ArrayList,
				// so stop with this string.
				if (jj >= curr.length())
				{
					doneIn = true;
				}
					// Current character in the key is less than the current
					// character in the string in the ArrayList.  Since the
					// ArrayList is in alphabetical order, this means we can
					// stop our search altogether.
				else if (s.charAt(j) < curr.charAt(jj))
				{
					doneIn = true;
					doneOut = true;
				}
				else 
				{
					currTest = (s.charAt(j) == curr.charAt(jj));
					if (currTest && j == end && jj < curr.length()-1)
					{
						prefix = true;  // Chars match and we are at end of
						doneIn = true;  // key but not end of String
					}
					else if (currTest && j == end && jj == curr.length()-1)
					{
						word = true;    // Chars match and we are at end of
						doneIn = true;  // key and end of String
					}
					else if (!currTest)
					{
						doneIn = true;  // Chars do not match
					}
				}
			}
		}
		if (prefix && word) return 3;
		else if (word) return 2;
		else if (prefix) return 1;
		else return 0;
	}
}