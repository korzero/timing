package korzero.timing.springbook.embeddeddb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/context-database.xml" })
public class EmbeddedBbTestForSpring {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void initData() {
		assertThat(jdbcTemplate.queryForObject(
				"select count(*) from sqlmap where key_ in('KEY1', 'KEY2')",
				Integer.class), is(2));

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
