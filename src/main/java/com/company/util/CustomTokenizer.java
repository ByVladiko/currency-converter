package com.company.util;

import java.util.List;

public class CustomTokenizer {

    private final String str;
    private int currentPosition;
    private final List<String> delims;
    private int tokenPosition;
    private boolean returnDelims;

    public CustomTokenizer(String input, List<String> delims, boolean returnDelims) {
        currentPosition = 0;
        tokenPosition = 0;
        this.str = input;
        this.delims = delims;
        this.returnDelims = returnDelims;
    }

    public Token nextToken() {
        StringBuilder result = new StringBuilder();
        for (tokenPosition = currentPosition; currentPosition < str.length(); currentPosition++) {
            char currentChar = str.charAt(currentPosition);
            if (delims.contains(Character.toString(currentChar))) {
                if (result.length() > 0) {
                    return new Token(result.toString(), tokenPosition);
                }

                if (returnDelims) {
                    currentPosition++;
                    return new Token(Character.toString(currentChar), tokenPosition);
                }

                continue;
            }

            if (result.length() == 0)
                tokenPosition = currentPosition;

            result.append(currentChar);
        }

        return new Token(result.toString(), tokenPosition);
    }

    public boolean hasNext() {
        boolean hasNext = !(currentPosition == str.length() - 1
                && delims.contains(Character.toString(str.charAt(currentPosition))));
        return returnDelims ? currentPosition < str.length() : currentPosition < str.length() && hasNext;
    }

    public void reset() {
        currentPosition = 0;
        tokenPosition = 0;
    }

    public void setReturnDelims(boolean returnDelims) {
        this.returnDelims = returnDelims;
    }

    public static class Token {
        private final String token;
        private final int position;

        public Token(String token, int position) {
            this.token = token;
            this.position = position;
        }

        public String getToken() {
            return token;
        }

        public int getPosition() {
            return position;
        }
    }
}
