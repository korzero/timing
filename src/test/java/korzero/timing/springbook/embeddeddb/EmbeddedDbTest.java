package korzero.timing.springbook.embeddeddb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * <pre>
 * ������ DB�� Application �� ����ż� Application �� �Բ� ���۵ǰ� ����Ǵ� DB�� ���Ѵ�.
 * 
 * Spring Framework ������ ������ DB�� �ʱ�ȭ�ϴ� �۾��� �����ϴ� ���� ������ DB������ �����Ѵ�.
 * 
 * </pre>
 * 
 * @author korzero
 *
 */
public class EmbeddedDbTest {

	EmbeddedDatabase dataSource;

	JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() {

		// Spring�� ������ DB ������ ����ϴ� �������� �ڵ�
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:/database/timing-script.sql")
				.addScript("classpath:/database/timing-data.sql").build();

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@After
	public void tearDown() {
		dataSource.shutdown();
	}

	@Test
	public void initData() {
		assertThat(
				jdbcTemplate.queryForObject("select count(*) from sqlmap", Integer.class),
				is(2));

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select * from sqlmap order by key_");

		assertThat(list.get(0).get("key_").toString(), is("KEY1"));
		assertThat(list.get(0).get("sql_").toString(), is("SQL1"));

		assertThat(list.get(1).get("key_").toString(), is("KEY2"));
		assertThat(list.get(1).get("sql_").toString(), is("SQL2"));

	}

	@Test
	public void insert() {
		jdbcTemplate
				.update("insert into sqlmap(key_, sql_) values(?, ?)", "KEY3", "SQL3");

		assertThat(
				jdbcTemplate.queryForObject("select count(*) from sqlmap", Integer.class),
				is(3));
	}
}
