package dk.easv.mytunes.exceptions;

import java.security.cert.Extension;

public class MainException extends Exception
{
  public MainException(String errorMessage){
    super(errorMessage);
    this.printStackTrace();
  }
}
