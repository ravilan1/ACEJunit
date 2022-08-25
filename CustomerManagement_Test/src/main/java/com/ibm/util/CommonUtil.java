package com.ibm.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ibm.bean.TestCaseBean;
import com.ibm.integration.test.v1.NodeSpy;
import com.ibm.integration.test.v1.NodeStub;
import com.ibm.integration.test.v1.SpyObjectReference;
import com.ibm.integration.test.v1.TestMessageAssembly;
import com.ibm.integration.test.v1.exception.TestException;


public class CommonUtil {
	
	//Reading properties file for required properties
	public Properties readProperties(String fName) {
		Properties p = null;
		FileReader freader = null;
		try {
			freader = new FileReader(fName);
			p = new Properties();
			p.load(freader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	//Getting message assembly based on input file from property file
	public TestMessageAssembly getMesgAssembly(String nodeType,String fileName) throws TestException {
		TestMessageAssembly msgAssembly = new TestMessageAssembly();
		Properties prop = readProperties(fileName);
		try {
			switch (nodeType) {
			case "input":
				System.out.println("property file--->"+prop.getProperty("inputfile"));
				InputStream inMessageStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(prop.getProperty("inputfile"));
				msgAssembly.buildFromRecordedMessageAssembly(inMessageStream);
				return msgAssembly;
			case "output":
				System.out.println("property file--->"+prop.getProperty("outputfile"));
				InputStream outMessageStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(prop.getProperty("outputfile"));
				msgAssembly.buildFromRecordedMessageAssembly(outMessageStream);
				return msgAssembly;
			case "propagate":
				System.out.println("property file--->"+prop.getProperty("propagatefile"));
				InputStream propagateMessageStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(prop.getProperty("propagatefile"));
				msgAssembly.buildFromRecordedMessageAssembly(propagateMessageStream);
				return msgAssembly;
			case "stub":
				System.out.println("property file--->"+prop.getProperty("stubfile"));
				InputStream stubMessageStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(prop.getProperty("stubfile"));
				msgAssembly.buildFromRecordedMessageAssembly(stubMessageStream);
				return msgAssembly;
			default:
				throw new TestException("Unable to locate message assembly file: ");
			}
		}catch(Exception ex) {
			throw new TestException("Failed to compare with expected message", ex);
		}
	}
	
	//Setting values to bean from properties file
	public TestCaseBean setProperties(String fileName) throws IOException {
		TestCaseBean tbean = null;
		tbean = new TestCaseBean();
		Properties p = readProperties(fileName);
		tbean.setAplication(p.getProperty("Application"));
		tbean.setFlow(p.getProperty("Flow"));
		tbean.setSubflow(p.getProperty("subflow"));
		tbean.setInputnode(p.getProperty("inputnode"));
		tbean.setOutputnode(p.getProperty("outputnode"));
		tbean.setStubnode(p.getProperty("stubnode"));
		tbean.setInputfile(p.getProperty("inputfile"));
		tbean.setOutputfile(p.getProperty("outputfile"));
		tbean.setStubfile(p.getProperty("stubfile"));
		return tbean;
	}
	
	//Getting nodespy based on spyobject
	public NodeSpy getNodeSpy(String node,String fileName) throws IOException {
		NodeSpy spy = null;
		SpyObjectReference outrefIn=null;
		TestCaseBean caseBean= setProperties(fileName);
		try {
			switch (node) {
			case "input":
				if(caseBean.getSubflow()!=null) {
				outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).subflowNode(caseBean.getSubflow()).node(caseBean.getInputnode());
				}else {
					outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).node(caseBean.getInputnode());
				}
				spy = new NodeSpy(outrefIn);
				break;
			case "output":
				if(caseBean.getSubflow()!=null) {
				outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).subflowNode(caseBean.getSubflow()).node(caseBean.getOutputnode());
				}else {
					outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).node(caseBean.getOutputnode());
				}
				spy = new NodeSpy(outrefIn);
				break;
			default:
				break;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return spy;
	}
	
	//Getting nodestub based on spyobject
	public NodeStub getNodeStub(String node,String fileName) throws IOException {
		NodeStub stub = null;
		SpyObjectReference outrefIn=null;
		TestCaseBean caseBean= setProperties(fileName);
		try {
			switch (node) {
			case "stub":
				if(caseBean.getSubflow()!=null) {
				outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).subflowNode(caseBean.getSubflow()).node(caseBean.getStubnode());
				}else {
					outrefIn = new SpyObjectReference().application(caseBean.getAplication()).messageFlow(caseBean.getFlow()).node(caseBean.getStubnode());
				}
				stub = new NodeStub(outrefIn);
				break;
			default:
				break;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return stub;
	}
	
	/*
	public static void main(String[] args) throws IOException {
		CommonUtil util = new CommonUtil();
		TestCaseBean caseBean= util.setProperties();
		System.out.println("Subflow name is-->"+caseBean.getSubflow());
	}*/

}
