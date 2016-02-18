package cc.trity.library.net;

/**
 * 约定好返回数据的格式
 * Created by TryIT on 2016/2/5.
 */
public class Response {
    private boolean error;
    private int errorType;	//1为Cookie失效
    private String errorMessage;
    private String result;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
