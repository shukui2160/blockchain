package com.tlabs.blockchain.model;

public class Block {
	//����ID
	private String BlockID;
	//����߶�
	private String Height;
	private String Hash;
	//��һ����ϣ
	private String LastHash;
	//���˶���
	private String Melk;
	//�����
	private String nounce;
	public String getBlockID() {
		return BlockID;
	}
	public void setBlockID(String blockID) {
		BlockID = blockID;
	}
	public String getHeight() {
		return Height;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public String getHash() {
		return Hash;
	}
	public void setHash(String hash) {
		Hash = hash;
	}
	public String getLastHash() {
		return LastHash;
	}
	public void setLastHash(String lastHash) {
		LastHash = lastHash;
	}
	public String getMelk() {
		return Melk;
	}
	public void setMelk(String melk) {
		Melk = melk;
	}
	public String getNounce() {
		return nounce;
	}
	public void setNounce(String nounce) {
		this.nounce = nounce;
	}
	
}
