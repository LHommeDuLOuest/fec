#!
#~bin~ ./spark-class org.apache.spark.executor.CoarseGrainedExecutorBackend --driver-url spark://CoarseGrainedScheduler@10.0.2.15:35496 --executor-id 28 --hostname localhost --cores 41 --app-id app-20170914105902-0000 --worker-url spark://Worker@localhost:34117


red=`tput setaf 1`
green=`tput setaf 2`
oran=`tput setaf 3`
bleu=`tput setaf 4`
pink=`tput setaf 5`
reset=`tput sgr0`
bold=`tput bold`
underline=`tput smul`
dis_underline=`tput rmul`
bel=`tput bel`


echo "${reset} ${green} ${bold} ${underline}                                                                             ${dis_underline} ${reset}"
echo "${reset} ${green} ${bold}      |==================== ${underline} Welcome to FEC-APP ${dis_underline} ====================| ${reset}"
echo "${reset} ${green} ${bold} ${underline}                                                                             ${dis_underline} ${reset}"
echo ""
echo ""

echo "${reset} ${green} +[JAVA_HOME]: $JAVA_HOME ${reset}"
echo "${reset} ${green} ++[JAVA]:" 
                                        java -version ${reset}
echo ""
echo "${reset} ${green} +[MAVEN_HOME]: $M2_HOME ${reset}"
echo "${reset} ${green} ++[MAVEN]:" 
                                          mvn -version ${reset}
echo ""
echo ""

#logger -s "${oran} ${bold} [warn]-- SPARK_HOME is missed ${reset}"
echo "${oran} ${bold} [warn]-- SPARK_HOME is missed ${reset}"
echo "${green}[info]-- Please input the path of SPARK_HOME ${reset}"
read SPARK_HOME
echo "${green} [info]-- SPARK_HOME=$SPARK_HOME ${reset}"
echo "${bleu}" 
ls $SPARK_HOME
echo "${reset}" 

echo "${green} ${bold} =====  Starting APACHE SPARK MASTER...  ===== ${reset}"
cd $SPARK_HOME/sbin
./start-master.sh
echo "${green}[info]-- Spark master is started in standalone mode ${reset}"
#cd $SPARK_HOME/sbin
#./stop-all.sh

gnome-terminal -x sh -c "
echo '${green} ${bold} ===  Starting APACHE SPARK WORKER 1 ...  === ${reset}'
cd $SPARK_HOME/bin
./spark-class org.apache.spark.deploy.worker.Worker spark://lyes:7077 --cores 46 --memory 2G -h localhost"
echo "${green}[info]-- worker 1 is started ${reset}"


gnome-terminal -x sh -c "
echo '${green} ${bold} ===  Starting APACHE SPARK WORKER 2 ...  === ${reset}'
cd $SPARK_HOME/bin
./spark-class org.apache.spark.deploy.worker.Worker spark://lyes:7077 --cores 46 --memory 3G -h localhost"
echo "${green}[info]-- worker 2 is started ${reset}"

#gnome-terminal -x sh -c "
#echo '${green} ${bold} ===  Starting APACHE SPARK WORKER 3 ...  === ${reset}'
#cd $SPARK_HOME/bin
#./spark-class org.apache.spark.deploy.worker.Worker spark://localhost:7077 --cores 11 --memory 1G -h localhost"
#echo "${green}[info]-- worker 3 is started ${reset}"

#gnome-terminal -x sh -c "
#echo '${green} ${bold} ===  Starting APACHE SPARK WORKER 4 ...  === ${reset}'
#cd $SPARK_HOME/bin
#./spark-class org.apache.spark.deploy.worker.Worker spark://localhost:7077 --cores 11 --memory 1G -h localhost"
#echo "${green}[info]-- worker 4 is started ${reset}"

echo "${bleu} ============================================================ ${reset}"
echo "${bleu} ============================================================ ${reset}"

echo "${green} ${bold} =====  BUILD FEC-APP...  ===== ${reset}"
echo "${oran} ${bold} [warn]-- FEC_HOME is missed ${reset}"
echo "${green}[info]-- Please input the path of FEC_HOME ${reset}"
read FEC_HOME
echo "${green} [info]-- FEC_HOME=$FEC_HOME ${reset}"
echo "${red}" 
ls $FEC_HOME
echo "${reset}" 

#FEC-HOME= /opt/codes/fbd
gnome-terminal -x sh -c "
echo '${green} ${bold} ===  fec-app install ...  === ${reset}'
cd $FEC_HOME/fec-parent
mvn -T 4C -DskipTests clean install -pl \!fec-front

echo '${bleu} ============================================================ ${reset}'
echo '${bleu} ============================================================ ${reset}'

echo '${green} ${bold} ===  Starting FEC-APP ...  === ${reset}'
cd $FEC_HOME/fec-parent/fec-web
java -Xms2560m -Xmx4900m -server  -javaagent:$FEC_HOME/fec-parent/fec-service/aspectjweaver-1.8.10.jar -javaagent:$FEC_HOME/fec-parent/fec-service/spring-instrument-4.3.6.RELEASE.jar -jar target/fec-web-0.0.1.RELEASE.jar"


echo "${bleu} ============================================================ ${reset}"
echo "${bleu} ============================================================ ${reset}"

gnome-terminal -x sh -c "
echo '${green} ${bold} ===  fec-app web client build ...  === ${reset}'
cd $FEC_HOME/fec-parent/fec-front/src/main/frontend
ng serve --open"

#cd $SPARK_HOME/sbin
#./stop-all.sh

