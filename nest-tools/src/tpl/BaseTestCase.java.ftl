package ${hss_base_package};

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public abstract class BaseTestCase extends
    AbstractTransactionalSpringContextTests
{

	DriverManagerDataSource dataSource;


	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}

    <#if hss_jdk5>@Override</#if>
    protected String[] getConfigLocations()
    {
        this.setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {
        		"file:WebContent\\WEB-INF\\applicationContext-*.xml",
        		"file:WebContent\\WEB-INF\\datasource-context-test.xml"
        		};
    }
    
	<#if hss_jdk5>@Override</#if>
	protected void onSetUpInTransaction() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(
				dataSource.getConnection());
		XmlDataSet dataset = new XmlDataSet(
				applicationContext.getResource("file:conf\\test-data.xml").getInputStream());
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		super.onSetUpInTransaction();
	}

}
