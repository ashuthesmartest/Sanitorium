/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.Tab;

import com.DBConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

public class StatsTabController implements Initializable
{
    
 private ObservableList<ObservableList> data;
 private ObservableList chartData = FXCollections.observableArrayList();
    
@FXML
ComboBox cb = new ComboBox(); 
 
@FXML
CategoryAxis xAxis1 = new CategoryAxis() ;

@FXML
NumberAxis yAxis1 = new NumberAxis() ;

@FXML
CategoryAxis xAxis2 = new CategoryAxis() ;

@FXML
NumberAxis yAxis2 = new NumberAxis() ;    

@FXML
CategoryAxis xAxis3 = new CategoryAxis() ;

@FXML
NumberAxis yAxis3 = new NumberAxis() ;

@FXML
public PieChart pieChart = new PieChart();

@FXML
public Pane ap = new Pane() ;

@FXML
public BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis1,yAxis1);

@FXML
public LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis2,yAxis2);

@FXML
public AreaChart<String,Number> areaChart = new AreaChart<String,Number>(xAxis3,yAxis3);
   
XYChart.Series serie = new XYChart.Series();
XYChart.Series serie1 = new XYChart.Series();
XYChart.Series serie2 = new XYChart.Series();

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, IOException, InterruptedException 
    {
        String out = cb.getSelectionModel().getSelectedItem().toString() ;
        if(out.equals("PieChart"))
        {
            ap.getChildren().clear();
            pieChart.getData().clear();
            buildPieChart();
            ap.setVisible(true);
        }
        if(out.equals("BarChart"))
        {   
            barChart.getData().removeAll(serie) ;
            ap.getChildren().clear();
            buildBarChart();
        }    
        if(out.equals("LineChart"))
        {   
            lineChart.getData().removeAll(serie1) ;
            ap.getChildren().clear();
            buildLineChart();
        }
        if(out.equals("AreaChart"))
        {   
            areaChart.getData().removeAll(serie2) ;
            ap.getChildren().clear();
            buildAreaChart();
        }
    }
    
    public void buildPieChart() 
    {
        pieChart.getData().clear();
        chartData.clear();
        try{
            String SQL = "select cast(awards as decimal)/years as "+"\"ave\""+", name from app_doctor order by "+"\"ave\"";
            ResultSet rs = DBConnect.st.executeQuery(SQL);
            while(rs.next())
            {
                chartData.add(new PieChart.Data(rs.getString(2),rs.getDouble(1)));
            }
        }
        catch(Exception e)
        {
            System.out.println("Error on DB connection: " + e);
        }
        pieChart.getData().addAll(chartData);
        ap.getChildren().addAll(pieChart) ;
        pieChart.setLegendVisible(false);
    }

    public void buildBarChart() 
    {
        chartData = FXCollections.observableArrayList();
        serie.setName("Performance per year");
        try{
            String SQL = "select (awards * 10)/years as "+"\"ave\""+", name from app_doctor order by "+"\"ave\"";
            ResultSet rs = DBConnect.st.executeQuery(SQL);

            while(rs.next())
            {
                serie.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println("Error on DB connection: " + e);
        }
        barChart.getData().addAll(serie);
        ap.getChildren().addAll(barChart) ;
    }

    public void buildLineChart() 
    {
        serie1.setName("Performance per year");
        try{
            String SQL = "select (awards * 10)/years as "+"\"ave\""+", name from app_doctor order by "+"\"ave\"" ;
            ResultSet rs = DBConnect.st.executeQuery(SQL);

            while(rs.next()){
                serie1.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println("Error on DB connection: " + e);
        }
        lineChart.getData().addAll(serie1);
        ap.getChildren().addAll(lineChart) ;
    }
    public void buildAreaChart() 
    {
        serie2.setName("Performance per year");
        try{
            String SQL = "select (awards * 10)/years as "+"\"ave\""+", name from app_doctor order by "+"\"ave\"" ;
            ResultSet rs = DBConnect.st.executeQuery(SQL);

            while(rs.next()){
                serie2.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println("Error on DB connection: " + e);
        }
        areaChart.getData().addAll(serie2);
        ap.getChildren().addAll(areaChart) ;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        buildPieChart();
        cb.getItems().addAll("PieChart", "LineChart", "BarChart", "AreaChart") ;
    }


    
}
