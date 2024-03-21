import java.io.*;

public class SciCalcHistory {
    public String expression;
	
    public String ans;
	
    public int dataPlusExpression;
	
    public int dataPlusAns;
	
    public int dataS;
	
    public String [] dataExpression = new String [dataPlusExpression];
	
    public String [] dataAns = new String [dataPlusAns];
	
	public boolean t = true;

    public void write(String expression, String ans, int data) throws IOException {

        File history = new File("HistoryLog.doc");
		history.createNewFile();
		FileWriter historyfw = new FileWriter(history);
		
		dataS = data;

        do {
			dataExpression[dataPlusExpression] = expression;
			dataAns[dataPlusAns] = ans;
			
			historyfw.write(dataExpression[dataPlusExpression] + " "+ dataAns[dataPlusAns]+"\n");
			dataPlusExpression++;
			dataPlusAns++;
			
		} while(dataPlusAns != dataS);
		historyfw.close();
    }
    	
	public int dataSplus() {
		return dataS;
	}
}
