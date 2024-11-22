package berry.tetra.model;

import java.util.List;

public class QuizQuestion {
  private String word;
  private String correctMean;
  private List<String> options;

  public QuizQuestion(String word, String correctMean, List<String> options) {
    this.word = word;
    this.correctMean = correctMean;
    this.options = options;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getCorrectMean() {
    return correctMean;
  }

  public void setCorrectMean(String correctMean) {
    this.correctMean = correctMean;
  }

  public List<String> getOptions() {
    return options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

}
