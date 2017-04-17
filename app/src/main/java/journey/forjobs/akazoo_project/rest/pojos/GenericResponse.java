package journey.forjobs.akazoo_project.rest.pojos;

/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class GenericResponse {
    private String ErrorData;
    private int ErrorId;
    private boolean isError;

    public String getErrorData() {
        return ErrorData;
    }

    public void setErrorData(String errorData) {
        ErrorData = errorData;
    }

    public int getErrorId() {
        return ErrorId;
    }

    public void setErrorId(int errorId) {
        ErrorId = errorId;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
