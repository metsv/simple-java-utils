package ee.metsv;

/**
 * Created with IntelliJ IDEA.
 * User: metsv
 * Date: 8/19/13
 * Time: 8:22 AM
 */
public class CamelCaseMatcher {

    private final char STAR = '*';

    private final String className;
    private final String packageName;

    private String pattern;

    private int searchStartIndexInClassName;
    private int matchedCharIndexInClassName;

    private int lastUsedClassNameUpperCaseIndex;
    private int lastUsedPatternUpperCaseIndex;

    public CamelCaseMatcher(String className) {
        this.className = className;
        this.packageName = null;
    }

    public CamelCaseMatcher(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    public String getFullName(){
        if (packageName == null) return this.className;
        else return this.packageName.concat(this.className);
    }

    public boolean match(String pattern) {

        boolean isCharMatchFound = false;

        initializeIndexes();
        this.pattern = pattern;

        for (int patternIndex = 0; patternIndex < pattern.length(); patternIndex++) {
            char patternChar = pattern.charAt(patternIndex);
            if (Character.isUpperCase(patternChar))
                isCharMatchFound = matchUpperCaseChar(patternIndex);
            else if (patternChar == STAR) {
                isCharMatchFound = true;
                continue;
            } else isCharMatchFound = matchUpperOrLowerCaseChar(patternIndex);


            if (!isCharMatchFound && isMatchAfterPreviousUpperCaseIndex()) {
                patternIndex = rollBackPatternIndexAndMatchStartIndexToAfterLastUppercaseMatch();
                continue;
            }

            if (!isCharMatchFound && !(isSpaceLastPatternCharAndIsEndOfClassName(patternIndex)))
                return false;

            searchStartIndexInClassName = matchedCharIndexInClassName + 1;
        }
        return true;
    }

    private void initializeIndexes() {
        this.searchStartIndexInClassName = 0;
        this.matchedCharIndexInClassName = -1;
        this.lastUsedClassNameUpperCaseIndex = 0;
        this.lastUsedPatternUpperCaseIndex = 0;
    }

    private boolean matchUpperCaseChar(int patternIndex) {        
        matchedCharIndexInClassName = className.indexOf(pattern.charAt(patternIndex), searchStartIndexInClassName);
        
        if (isMatchFound(matchedCharIndexInClassName)) {
            lastUsedClassNameUpperCaseIndex = matchedCharIndexInClassName;
            lastUsedPatternUpperCaseIndex = patternIndex;
        }
        return isMatchFound(matchedCharIndexInClassName);
    }

    private boolean matchUpperOrLowerCaseChar(int patternIndex) {

        int lowerCaseCharMatchIndex = matchLowerCaseChar(patternIndex);
        int upperCaseCharMatchIndex = className.indexOf(String.valueOf(pattern.charAt(patternIndex)).toUpperCase(), searchStartIndexInClassName);

        matchedCharIndexInClassName = getFirstMatchIndex(lowerCaseCharMatchIndex, upperCaseCharMatchIndex);

       if (isMatchFound(matchedCharIndexInClassName) && upperCaseCharMatchIndex == matchedCharIndexInClassName) {
            lastUsedClassNameUpperCaseIndex = matchedCharIndexInClassName;
            lastUsedPatternUpperCaseIndex = patternIndex;
        }
        return isMatchFound(matchedCharIndexInClassName);
    }

   
    private int getFirstMatchIndex(int lowerCaseMatchIndex, int upperCaseMatchIndex) {
        if (lowerCaseMatchIndex > 0 && upperCaseMatchIndex > 0)
            return Math.min(lowerCaseMatchIndex, upperCaseMatchIndex);
        else return Math.max(lowerCaseMatchIndex, upperCaseMatchIndex);
    }

    private int matchLowerCaseChar(int patternIndex) {        
        int lowerCaseMatchIndex = className.indexOf(pattern.charAt(patternIndex), searchStartIndexInClassName);
        if (isMatchFound(lowerCaseMatchIndex) && (isNextToPreviousMatchedChar(lowerCaseMatchIndex) || wasPreviousPatternCharStarChar(patternIndex)))
            return lowerCaseMatchIndex;
        else return -1;
    }

    private boolean wasPreviousPatternCharStarChar(int patternIndex) {
        return patternIndex > 0 && pattern.charAt(patternIndex - 1) == STAR;
    }

    private boolean isNextToPreviousMatchedChar(int lowerCaseMatchIndex) {
        return lowerCaseMatchIndex - 1 == matchedCharIndexInClassName || matchedCharIndexInClassName == -1;
    }


    private boolean isMatchAfterPreviousUpperCaseIndex() {
        boolean isIndexInBounds = lastUsedClassNameUpperCaseIndex + 1 < className.length();
        boolean sameMatchExistsAfterLastUpperCaseMatch = className.indexOf(pattern.charAt(lastUsedPatternUpperCaseIndex), lastUsedClassNameUpperCaseIndex + 1) >= 0;
        return isIndexInBounds && sameMatchExistsAfterLastUpperCaseMatch;
    }

    private int rollBackPatternIndexAndMatchStartIndexToAfterLastUppercaseMatch() {
        int rolledBackPatternIndex = lastUsedPatternUpperCaseIndex - 1;
        lastUsedClassNameUpperCaseIndex++;
        searchStartIndexInClassName = lastUsedClassNameUpperCaseIndex;
        return rolledBackPatternIndex;
    }

    private boolean isSpaceLastPatternCharAndIsEndOfClassName(int patternIndex) {
        return Character.isSpaceChar(pattern.charAt(patternIndex)) && patternIndex == pattern.length() - 1 && searchStartIndexInClassName == className.length();
    }

    private boolean isMatchFound(int Index) {
        return Index < 0 ? false : true;
    }
}
