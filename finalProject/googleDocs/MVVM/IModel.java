package shenkar.finalProject.googleDocs.MVVC;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;

public interface IModel  {
    /**
     * @param viewModel - the viewModel of the project
     * @throws MVVMException - customise Exception
     */
    public void setIViewModel(IViewModel viewModel) throws MVVMException;

    /**
     * @param service - the main object who responsible to create Docs
     * @param docName - the name of the new Doc
     * @return - the new Doc ID
     * @throws MVVMException - customise Exception
     */
    public String createDoc(Docs service, String docName) throws MVVMException;

    /**
     * @return current service
     * @throws MVVMException - customise Exception
     */
    public Docs getDocsService() throws MVVMException;

    /**
     * @param response - curr Doc
     * @return the Doc's title
     */
    public String getDocTitle(Document response);

    /**
     * @param service - the main object who responsible to create Docs
     * @param docId - the Doc id
     * @param textToInsert - inserting template text
     * @throws MVVMException - customise Exception
     */
    public void setBodyTemplateText(Docs service, String docId, String textToInsert) throws MVVMException;

    /**
     * @param docsService - the main object who responsible to create Docs
     * @param docId - the Doc id
     * @param authorName - the name of the creator from UI
     * @param summery - the summery from UI
     * @param toDos - the bullets from UI
     * @throws MVVMException - customise Exception
     */
    public void replaceTemplatedText(Docs docsService, String docId , String authorName, String summery, String toDos) throws MVVMException;

    /**
     * @param service - the main object who responsible to create Docs
     * @param docId - the Doc id
     * @throws MVVMException  - customise Exception
     * will insert the Doc style
     */
    public void setDocStyle(Docs service, String docId) throws MVVMException;
}



