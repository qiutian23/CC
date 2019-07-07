package com.myself.CC.analyze.dao.impl;

import com.myself.CC.analyze.dao.MessageDao;
import com.myself.CC.analyze.entity.PoetryInfo;
import com.myself.CC.analyze.model.AuthorCount;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    private final DataSource dataSource;

    public MessageDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //查询作者及对应的诗文创作数量
    @Override
    public List<AuthorCount> getAuthorCountMessage() {
        List<AuthorCount> datas=new LinkedList<>();
        String sql="select count(*) as count , author from poetry_info group by author";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();) {
            while (resultSet.next()){
                AuthorCount authorCount=new AuthorCount();
                authorCount.setAuthor(resultSet.getString("author"));
                authorCount.setCount(resultSet.getInt("count"));
                datas.add(authorCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    //查询所有信息
    @Override
    public List<PoetryInfo> getAllPoetryMessage() {
        List<PoetryInfo> datas=new LinkedList<>();
        String sql="select title , dynasty , author , content from poetry_info";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();){ //自动关闭
            while (resultSet.next()){
                PoetryInfo poetryInfo=new PoetryInfo();
                poetryInfo.setTitle(resultSet.getString("title"));
                poetryInfo.setDynasty(resultSet.getString("dynasty"));
                poetryInfo.setAuthor(resultSet.getString("author"));
                poetryInfo.setContent(resultSet.getString("content"));
                datas.add(poetryInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
