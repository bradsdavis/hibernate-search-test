package com.bradsdavis.service;

import java.util.Collection;

import javax.ejb.EJB;

import org.hibernate.search.query.facet.Facet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bradsdavis.dao.CatalogDao;
import com.bradsdavis.dao.CategoryDao;
import com.bradsdavis.dao.ItemDao;
import com.bradsdavis.jpa.model.Catalog;
import com.bradsdavis.jpa.model.Category;
import com.bradsdavis.jpa.model.Item;

@RunWith(Arquillian.class)
public class ItemServiceTest {

	private final int UPPER_LIMIT = 20;
	
	@EJB
	ItemDao itemDao;
	
	@EJB
	CatalogDao catalogDao;
	
	@EJB
	CategoryDao categoryDao;
	
	@EJB
	ItemSearchService itemSearch;
	
	
	@Deployment
    public static WebArchive createDeployment() {
		MavenDependencyResolver resolver = DependencyResolvers.use(
		MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
				
		
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "test-hibernate-search.war")    
			.addAsManifestResource("jbossas-ds.xml");
		
		
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test-hibernate-search.jar")
            .addClasses(ItemDao.class)
            .addPackages(true, "com.bradsdavis")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("test-persistence.xml", "persistence.xml");
        
		archive.addAsLibraries(jar);
		archive.addAsLibraries(resolver.artifact("org.hibernate:hibernate-search").resolveAsFiles());
	    
		return archive;
    }
	
    @Test
    @InSequence(1)
    public void createItems() {
    	Catalog createCatalog = new Catalog();
    	createCatalog.setName("ExampleCatalog");

    	catalogDao.create(createCatalog);

    	Category category1 = new Category();
    	category1.setName("123 Example ABC");
    	categoryDao.create(category1);
    	
    	Category category2 = new Category();
    	category2.setName("Item Example XYZ");
    	categoryDao.create(category2);
    	
    	System.out.println(category1);
    	System.out.println(category2);
    	
    	
    	for(int i=0; i<UPPER_LIMIT; i++) {
    		Item item = new Item();
    		item.setName("Example Item: "+i);
    		item.setCatalog(createCatalog);
    		
    		itemDao.create(item);
    		
    		item.getCategories().add(category1);
    		item.getCategories().add(category2);
    		
    		createCatalog.getItems().add(item);
    		
    		itemDao.update(item);
    	}
    	catalogDao.update(createCatalog);
    	
    	
    	//now, validate the objects exist in the database.
    	Catalog readCatalog = catalogDao.read(createCatalog.getId());
    	Assert.assertNotNull(readCatalog);
    	
    	
    }
    
    @Test
    @InSequence(2)
	public void testItemQuery() throws Exception {
		Collection<Item> items = itemSearch.searchByName("Example Item: 1");
		Assert.assertTrue(items.size() == 1);
	}
    
    @Test
    @InSequence(3)
	public void testItemQueryByCatalog() throws Exception {
		Collection<Item> items = itemSearch.searchByNameAndCatalog("Example Item: 1", "ExampleCatalog");
		System.out.println("Results: "+items.size());
		
		Assert.assertTrue(items.size() == 1);
	}

    
    @Test
    @InSequence(4)
	public void testMultipleItemFacets() throws Exception {
		Collection<Facet> facets = itemSearch.getFacets();
		
		Assert.assertTrue(facets.size() == 2);
		for(Facet facet : facets) {
			Assert.assertTrue("Count should be: "+UPPER_LIMIT+" but is: "+facet.getCount(), facet.getCount() == UPPER_LIMIT);
		}
	}
}
