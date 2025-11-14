#include <iostream>
#include <string>
#include <unordered_set>
#include <fstream>
#include <cctype>
#include <jni.h>
#include "com_convocraft_commandProcessor_profanityFilter.h"

using namespace std;

// Function to censor a word by replacing its characters with '*'
string censorWord(const string& word) {
    return string(word.size(), '*');
}

// Function to load bad words into an unordered_set from a file
unordered_set<string> loadBadWords(const string& filename) {
    unordered_set<string> badWords;
    ifstream file("../resources/" + filename); // Open the file

    if (!file.is_open()) {
        cerr << "Error: Could not open file " << filename << endl;
        return badWords;
    }

    string word;
    while (file >> word) { // Read each word and insert it into the set
        badWords.insert(word);
    }

    return badWords;
}

// Helper function to convert a string to lowercase
string toLower(const string& str) {
    string lowerStr;
    for (char ch : str) {
        lowerStr += tolower(ch);
    }
    return lowerStr;
}

// Helper function to remove punctuation from a word
string stripPunctuation(const string& word) {
    string cleanWord;
    for (char ch : word) {
        if (isalnum(ch)) { // Keep only alphanumeric characters
            cleanWord += ch;
        }
    }
    return cleanWord;
}

// Function to filter profanity from a message while retaining whitespace
string filterProfanityRetainingWhitespace(const string& message, const unordered_set<string>& badWords) {
    string filteredMessage;
    string currentWord;

    for (char ch : message) {
        if (isspace(ch)) {
            // When encountering a space, process the current word
            if (!currentWord.empty()) {
                string cleanWord = toLower(stripPunctuation(currentWord)); // Normalize the word
                if (badWords.find(cleanWord) != badWords.end()) {
                    filteredMessage += censorWord(currentWord); // Censor the word
                } else {
                    filteredMessage += currentWord; // Keep the word as is
                }
                currentWord.clear(); // Reset the current word
            }
            filteredMessage += ch; // Add the space to the result
        } else {
            // Build the current word character by character
            currentWord += ch;
        }
    }

    // Handle the last word (if there's no trailing whitespace)
    if (!currentWord.empty()) {
        string cleanWord = toLower(stripPunctuation(currentWord));
        if (badWords.find(cleanWord) != badWords.end()) {
            filteredMessage += censorWord(currentWord);
        } else {
            filteredMessage += currentWord;
        }
    }

    return filteredMessage;
}

string jstringToString(JNIEnv* env, jstring jStr) {
    const char* cStr = env->GetStringUTFChars(jStr, nullptr);
    string cppStr(cStr);
    env->ReleaseStringUTFChars(jStr, cStr);
    return cppStr;
}

// Function to convert a Java String array to a C++ unordered_set<string>
unordered_set<string> jstringArrayToSet(JNIEnv* env, jobjectArray jArray) {
    unordered_set<string> badWordsSet;
    jsize arrayLength = env->GetArrayLength(jArray);
    for (jsize i = 0; i < arrayLength; i++) {
        jstring jWord = (jstring)env->GetObjectArrayElement(jArray, i);
        badWordsSet.insert(jstringToString(env, jWord));
        env->DeleteLocalRef(jWord); // Free up memory
    }
    return badWordsSet;
}

// Native implementation of the function
JNIEXPORT jstring JNICALL Java_main_java_com_convocraft_commandProcessor_profanityFilter_filterProfanityRetainingWhitespace
  (JNIEnv* env, jobject obj, jstring jMessage, jobjectArray jBadWords) {
    // Convert Java inputs to C++ types
    string message = jstringToString(env, jMessage);
    unordered_set<string> badWords = jstringArrayToSet(env, jBadWords);

    // Call the C++ profanity filtering function
    string filteredMessage = filterProfanityRetainingWhitespace(message, badWords);

    // Convert the C++ string result back to a Java string
    return env->NewStringUTF(filteredMessage.c_str());
}
