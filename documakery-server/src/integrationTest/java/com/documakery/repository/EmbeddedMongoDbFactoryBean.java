package com.documakery.repository;

import java.io.IOException;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;

import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.UserTempNaming;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * EmbeddedMongoDbFactoryBean creates a MongoDbFactory with an embedded mongo instance.
 * 
 * @author Marco Jakob based on EmbedMongoDbFactoryBean by Waldemar Biller
 */
@Component
public class EmbeddedMongoDbFactoryBean implements FactoryBean<MongoDbFactory> {

  public static final String DATABASE_NAME = "embeddedMongo";
  public static final int PORT = 12345;
  
  private static MongodExecutable mongodExec;
  private static MongodProcess mongod;

  public void init() throws IOException {
    MongodConfig config = new MongodConfig(Version.Main.V2_2, PORT, Network.localhostIsIPv6());
    
    RuntimeConfig runtimeConfig = new RuntimeConfig();
    // Set executable naming to be the same for every run so we don't need to confirm the firewall dialog
    runtimeConfig.setExecutableNaming(new UserTempNaming());
    
    // Set up Logging
//    runtimeConfig.setProcessOutput(new ProcessOutput(Processors.logTo(LOG, Level.INFO),
//            Processors.logTo(LOG, Level.SEVERE), Processors.named("[console>]", Processors.logTo(LOG, Level.FINE))));
    
    MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
    mongodExec = runtime.prepare(config);
    mongod = mongodExec.start();
  }

  public void destroy() {
    if (mongod != null) {
      mongod.stop();
    }
    if (mongodExec != null) {
      mongodExec.stop();
    }
  }

  @Override
  public MongoDbFactory getObject() throws Exception {
    Mongo mongo = new Mongo("localhost", PORT);
    return new SimpleMongoDbFactory(mongo, DATABASE_NAME);
  }

  @Override
  public Class<?> getObjectType() {
    return MongoDbFactory.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}