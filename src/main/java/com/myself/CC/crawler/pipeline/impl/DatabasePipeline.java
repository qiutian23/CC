package com.myself.CC.crawler.pipeline.impl;

import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.pipeline.Pipeline;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//将数据存入数据库
public class DatabasePipeline implements Pipeline {
    private final DataSource dataSource;

    public DatabasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(Page page) {
        //取自己想要的存入数据库
        String title= (String) page.getDataSet().getData("title");
        String dynasty= (String) page.getDataSet().getData("dynasty");
        String author= (String) page.getDataSet().getData("author");
        String content= (String) page.getDataSet().getData("content");
        String sql="insert into poetry_info(title, dynasty, author, content) VALUES (?,?,?,?)";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1,title);
            statement.setString(2,dynasty);
            statement.setString(3,author);
            statement.setString(4,content);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
