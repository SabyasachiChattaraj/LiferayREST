package com.comerica.hook.sa.asset;

import java.io.StringReader;

import org.osgi.service.component.annotations.Component;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
@Component(
	 immediate=true,
	 service = ModelListener.class
)
public class ArpFormsAssetServiceWrapper extends BaseModelListener<JournalArticle>{

	
	@Override
	public void onAfterUpdate(JournalArticle model) throws ModelListenerException {

		if(model.isApproved()) {
			String myContent = model.getContent();

		    Document document = null;
		    String data=null;
		    try
		    {
		        document = SAXReaderUtil.read(new StringReader(myContent));
		        Node node = document.selectSingleNode("/root/dynamic-element[@name='data']/dynamic-content");
		        if (node.getText().length() > 0) {
		            data = node.getText();
		        }
		        System.out.println("data= "+data);
		    }
		    catch (Exception e)
		    {
		        e.printStackTrace();
		    }
		}
		super.onAfterUpdate(model);
	}
		
		
	
	

}
