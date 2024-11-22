package berry.tetra.model;

public class Quiz {
  int id;
  String word;
  String hinsi;
  String mean;

  public Quiz(int id, String word, String hinsi, String mean) {
    this.id = id;
    this.word = word;
    this.hinsi = hinsi;
    this.mean = mean;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getHinsi() {
    return hinsi;
  }

  public void setHinsi(String hinsi) {
    this.hinsi = hinsi;
  }

  public String getMean() {
    return mean;
  }

  public void setMean(String mean) {
    this.mean = mean;
  }
}
