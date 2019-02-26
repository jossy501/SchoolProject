/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author tony.ezeanya
 *
 */
public class E_STANDARD_SPLIT 
{
	
	private String channel_id;
	private String trans_code;
	private String description;
	private String owner_card;
	private String part_fee;
	private String split_type;
	private String etz_ratio;
	
	public E_STANDARD_SPLIT(){}

	

	public String getTrans_code() {
		return trans_code;
	}

	public void setTrans_code(String trans_code) {
		this.trans_code = trans_code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner_card() {
		return owner_card;
	}

	public void setOwner_card(String owner_card) {
		this.owner_card = owner_card;
	}

	public String getPart_fee() {
		return part_fee;
	}

	public void setPart_fee(String part_fee) {
		this.part_fee = part_fee;
	}

	public String getSplit_type() {
		return split_type;
	}

	public void setSplit_type(String split_type) {
		this.split_type = split_type;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}



	public String getEtz_ratio() {
		return etz_ratio;
	}



	public void setEtz_ratio(String etz_ratio) {
		this.etz_ratio = etz_ratio;
	}
	
	
	

}
