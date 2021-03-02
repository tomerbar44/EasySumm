package shenkar.finalProject.googleDocs.MVVC;

/**
 *
 */
public interface IView {
    /**
     * @param viewModel
     * @throws MVVMException
     */
    public void setIViewModel(IViewModel viewModel) throws MVVMException;
    public void textToModel(String titleName, String authorName, String summary, String bullets ) throws MVVMException;

    /**
     * @throws MVVMException
     */
    public void showWaitTillDocCreate() throws MVVMException;

    /**
     * @param newDocId
     * @throws MVVMException
     */
    public void showSuccessToCreateDoc(String newDocId) throws MVVMException;

    /**
     * @throws MVVMException
     */
    public void start() throws MVVMException;
}
