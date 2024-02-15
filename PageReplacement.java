//********************************************************************
//
//  Author:        Marshal Pfluger
//
//  Program #:     Program Eight
//
//  File Name:     PageReplacement.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      11/27/2023
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17
//
//  Description:    Class implements the algorithms  
//
//********************************************************************
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


class PageReplacement {
    private ArrayList<Integer> pageReference;
    private int numFrames;

    public PageReplacement(ArrayList<Integer> pageReferenceString, int numFrames) {
        this.pageReference = pageReferenceString;
        this.numFrames = numFrames;
    }

    //**************************************************************
    //
    //  Method:       runFIFO
    //
    //  Description:  executes the FIFO algorithm on the reference string
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runFIFO() {
        // Initialize a queue to represent the page frames
        Queue<Integer> pageQueue = new LinkedList<>();
        // Counter for page faults
        int pageFaults = 0;

        // Iterate through the page references
        for (int page : pageReference) {
            // If the page is not in the frames
            if (!pageQueue.contains(page)) {
                // If there is space in the frames, add the page
                if (pageQueue.size() < numFrames) {
                    pageQueue.add(page);
                } else {
                    // Remove the oldest page (First-In) from the frames
                    pageQueue.poll();
                    // Add the current page (Last-In) to the frames
                    pageQueue.add(page);
                }
                // Increment page faults counter
                pageFaults++;
            }
        }

        return pageFaults;
    }


    //**************************************************************
    //
    //  Method:       runLRU
    //
    //  Description:  executes the LRU algorithm on the reference string
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runLRU() {
        // Initialize a linked list to represent the page frames
        LinkedList<Integer> pageList = new LinkedList<>();
        // Map to track the presence of pages in the frames
        Map<Integer, Integer> pageMap = new HashMap<>();
        // Counter for page faults
        int pageFaults = 0;

        // Iterate through the page references
        for (int page : pageReference) {
            // If the page is not in the frames
            if (!pageMap.containsKey(page)) {
                // If there is space in the frames, add the page
                if (pageList.size() < numFrames) {
                    pageList.add(page);
                } else {
                    // Remove the least recently used page
                    int leastRecentlyUsed = pageList.poll();
                    pageMap.remove(leastRecentlyUsed);
                    // Add the current page to the frames
                    pageList.add(page);
                }
                // Increment page faults counter
                pageFaults++;
            } else {
                // If the page is already in the frames, move it to the end
                pageList.remove((Integer) page); // Remove the existing occurrence of the page
                pageList.add(page); // Move the page to the end to mark it as most recently used
            }

            // Update the map to indicate that the page is in the frames
            pageMap.put(page, 1);
        }

        return pageFaults;
    }

    //**************************************************************
    //
    //  Method:       runOPT
    //
    //  Description:  executes the OPT algorithm on the reference string
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runOpt() {
    	
        // Initialize a list to represent the page frames
        List<Integer> pageList = new ArrayList<>();
        // Counter for page faults
        int pageFaults = 0;

        // Iterate through the page references
        for (int i = 0; i < pageReference.size(); i++) {
            int page = pageReference.get(i);

            // If the page is not in the frames, a page fault occurs
            if (!pageList.contains(page)) {
                // Increment page faults counter
                pageFaults++;

                // If there are available frames, add the page to the frames
                if (pageList.size() < numFrames) {
                    pageList.add(page);
                } else {
                    // Find the page that will not be used for the longest period of time
                    int index = findOptimalPage(pageReference, pageList, i);

                    // Replace the page at the found index with the current page
                    pageList.set(index, page);
                }
            }
        }

        return pageFaults;
    }

    //**************************************************************
    //
    //  Method:       findOptimalPage
    //
    //  Description:  helper method for runOPT
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int findOptimalPage(List<Integer> pageReference, List<Integer> pageList, int currentIndex) {
    	
        int farthestIndex = -1;
        int farthestPage = -1;

        // Iterate through the pages in the frames
        for (Integer page : pageList) {
            // Find the next occurrence of the page in the page references
            int index = findNextOccurrence(pageReference, currentIndex, page);

            // If the page will not occur again, return the current index
            if (index == -1) {
                return pageList.indexOf(page);
            }

            // Update the farthest index and page if the current page has a farther occurrence
            if (index > farthestIndex) {
                farthestIndex = index;
                farthestPage = page;
            }
        }

        // Return the index of the page that will not be used for the longest period of time
        return pageList.indexOf(farthestPage);
    }

    //**************************************************************
    //
    //  Method:       findNextOccurrence
    //
    //  Description:  helper method for runOPT
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int findNextOccurrence(List<Integer> pageReference, int currentIndex, int page) {
        int nextOccurrenceIndex = -1;

        // Iterate through the page references starting from the current index
        for (int i = currentIndex + 1; i < pageReference.size() && nextOccurrenceIndex == -1; i++) {
            // If the page is found, update the next occurrence index
            if (pageReference.get(i) == page) {
                nextOccurrenceIndex = i;
            }
        }

        return nextOccurrenceIndex;
    }
}
