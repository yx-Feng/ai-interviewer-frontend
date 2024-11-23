package com.itzixi.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MPDBConfigOrg {

    public static void main(String[] args) {

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig
                .Builder("jdbc:mysql://127.0.0.1:4406/wechat-dev","root","root")
                .build();

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator(dsc);

        // 全局配置
        String projectPath = System.getProperty("user.dir");
        GlobalConfig gc = new GlobalConfig.Builder()
                .outputDir(projectPath + "/src/main/java")  // 生成路径
                .author("风间影月")     // 作者
                // .fileOverride()        // 多次生成是否覆盖之前的
                .build();
        autoGenerator.global(gc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig.Builder()
                .enableCapitalMode()        // 开启全局大小写(默认false)
                .enableSkipView()           // 开启跳过视图（默认false）
                .disableSqlFilter()         // 禁用 sql 过滤（默认true）
                .build();
        autoGenerator.strategy(strategy);

        // 包名策略配置
        PackageConfig pc = new PackageConfig.Builder()
                .parent("org.itzixi")
                .entity("pojo")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller")
                .build();
        autoGenerator.packageInfo(pc);

        // Entity 策略配置
        StrategyConfig entityConfig =  new StrategyConfig.Builder()
                .entityBuilder()
                .disableSerialVersionUID()
                // .enableLombok()
                .enableTableFieldAnnotation()
                // .enableActiveRecord()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .idType(IdType.ASSIGN_ID)
                // .formatFileName("%sEntity")
                .build();
        autoGenerator.strategy(entityConfig);

        // Service 策略配置
        StrategyConfig serviceConfig = new StrategyConfig.Builder()
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImp")
                .build();
        autoGenerator.strategy(serviceConfig);

        // Mapper 策略配置
        StrategyConfig mapperConfig =  new StrategyConfig.Builder()
                .mapperBuilder()
                .enableBaseResultMap()
                .enableBaseColumnList()
                // .formatMapperFileName("%sMapper")
                // .formatXmlFileName("%sXml")
                .build();
        autoGenerator.strategy(mapperConfig);

        // 执行逆向生成
        autoGenerator.execute();
    }
}
