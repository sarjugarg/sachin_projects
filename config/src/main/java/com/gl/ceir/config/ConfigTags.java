package com.gl.ceir.config;

public interface ConfigTags {
	// Message
	
	// System
	String sample_file_link = "sample_file_link";
	String upload_file_link = "upload_file_link";
	String file_consignment_download_dir = "file_consignment_download_dir";
	String file_consignment_download_link = "file_consignment_download_link";
	String file_stock_download_dir = "file_stock_download_dir";
	String file_stock_download_link = "file_stock_download_link";
	String file_stolen_and_recovery_dir = "file_stolen_and_recovery_dir";
	String file_stolen_and_recovery_download_link = "file_stolen_and_recovery_download_link";
	String file_custom_regularized_dir = "file_custom_regularized_dir";
	String file_custom_regularized_download_link = "file_custom_regularized_download_link"; 
	String file_audit_trail_download_dir = "file_audit_trail_download_dir";
	String file_audit_trail_download_link = "file_audit_trail_download_link";
	String new_year_date_register_device = "new_year_date_register_device";
	String grace_period_for_rgister_device = "grace_period_for_rgister_device";
	
	// User
	
	// Policy
	String max_end_user_device_count = "max_end_user_device_count";

}
