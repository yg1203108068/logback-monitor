-- Api权限
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (1, '查询信息', '/api/info/query', 'INFO_QUERY', '2023-02-17 20:01:54', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (2, '提交申请', '/api/info/submit', 'INFO_SUBMIT', '2023-02-17 20:01:54', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (3, '添加用户', '/api/user/add', 'USER_ADD', '2023-02-17 20:01:54', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (4, '编辑记录', '/api/info/edit', 'INFO_EDIT', '2023-02-17 20:01:54', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (5, '查看列表', '/api/info/list', 'INFO_LIST', '2023-02-17 20:28:36', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (6, '修改密码', '/api/password', 'USER_PASSWORD', '2023-02-17 22:00:20', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (7, '重置密码', '/api/user/reset/{id}', 'USER_RESET_PASSWORD', '2023-02-17 22:01:22', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (8, '用户管理', '/user/**', 'USER_LIST', '2023-02-17 22:01:22', NULL);
INSERT INTO ` logback_monitor `.` t_menu ` (` id `, ` name `, ` path `, ` code `, ` create_time `, ` update_time `)
VALUES (10, '我的提交', '/info/myList', 'MY_INFO_SUBMIT', '2023-02-17 22:01:22', NULL);


-- 角色
INSERT INTO ` logback_monitor `.` t_role ` (` id `, ` role_name `, ` code `, ` create_time `, ` update_time `)
VALUES (1, '用户', 'YONGHU', '2023-02-17 20:09:28', NULL);
INSERT INTO ` logback_monitor `.` t_role ` (` id `, ` role_name `, ` code `, ` create_time `, ` update_time `)
VALUES (2, '管理员', 'GUANLIYUAN', '2023-02-17 20:09:28', NULL);
INSERT INTO ` logback_monitor `.` t_role ` (` id `, ` role_name `, ` code `, ` create_time `, ` update_time `)
VALUES (3, '超级管理员', 'CHAOJIGUANLIYUAN', '2023-02-17 20:09:28', NULL);


-- 角色 和 Api 的关系
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (1, 1);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (2, 1);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (6, 1);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (10, 1);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (2, 2);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (4, 2);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (5, 2);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (6, 2);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (10, 2);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (2, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (3, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (4, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (5, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (6, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (7, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (8, 3);
INSERT INTO ` logback_monitor `.` t_role_menu ` (` menu_id `, ` role_id `)
VALUES (10, 3);


-- 超级管理员 admin2  密码：test
INSERT INTO ` logback_monitor `.` t_user ` (` id `, ` username `, ` password `, ` nickname `, ` last_login_time `, ` valid `, ` del `, ` locked `, ` public_key `, `
                                              private_key `, ` create_time `,
    ` create_by `, ` update_time `, ` update_by `)
VALUES (1, 'admin2', '$2a$10$0fUQ4Nwm/yoa.vNiY/VZf.OcVzhXge3Ue4t9vWM4hGcFreL0D26n2', '超级管理员', '2023-02-25 11:34:44', 1, b '0', 0,
        'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWawhWbHO7iLgqx+SX51RRFsrgWGbh2vE9XJXYB9GvVSZ6e0KzK+LqADMyhraWQ6od5rcZMuwg0VRQ33jdORt5oyDtv7Hj9JRNgIdOwdCm7wMi6ORCRR0LqGdrf+6JxV7p8xM8veS8WEPUvZPuTmVmghva0TTY+AKD7Oma/wrRsQIDAQAB',
        'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANZrCFZsc7uIuCrH5JfnVFEWyuBYZuHa8T1cldgH0a9VJnp7QrMr4uoAMzKGtpZDqh3mtxky7CDRVFDfeN05G3mjIO2/seP0lE2Ah07B0KbvAyLo5EJFHQuoZ2t/7onFXunzEzy95LxYQ9S9k+5OZWaCG9rRNNj4AoPs6Zr/CtGxAgMBAAECgYADHz1Yls0rmJ7H3q+9J3zijmCnWhorZTb4hAHZiqmasjR0eZl7xuKPsi+Mp4n+j58pPswBNazbAHlIESzvGa8E9FBLtO/h6VXmQy/IZCEsQdjnZmVgvY4nzagCOCL2uL2iKKfoPTBSFiG/sDz5mDbR/Cd/iQ1l5rs9vY1TYMU3nQJBAOUlNbwTR5ygWfxL735xvOQ8WyXE6ihtzrk/OQFfpKnhDpH4rUksMXk+3T/e9Zl7FCfdAgO3BmdhkCy+ns5lJcUCQQDvi/o3GqkphngnmS4O+SKCHW7zRSJXVIhNcEMWnsLY/6lEyH8zHKs1+/XztBPRk4yB9Fmgo/8yoLEBirXgaGb9AkAqocaijC+5mzfCsdoH+186U2Rd3Yn3JZ7dZj7T25iGJqIQVGThBUDbHvAI7xFgkb6JQBwc1tNxLnylZn2jRrxFAkEAqZdSODz50lpPKsm54APluHSaHkStwWIpdZmrBZK0o+isr/O6ijcbx4ZwZG7zMQbjDQYvhhlVDUTSzCKmDRAdCQJAKlWJnGKNinAevcgwa4DfC9KzsJ9Y18dfXnzOLKIRzjlpAYsgkX+oBZIV5wsoktDclPE3JwHUNrDM07TBNVM/NQ==',
        '2023-02-17 20:17:47', 'system', '2023-02-25 19:34:44', 'admin2');
-- 给超级管理员配置权限
INSERT INTO ` logback_monitor `.` t_user_role ` (` user_id `, ` role_id `)
VALUES (1, 3);


-- 备用超级管理员账号 by  密码:test
INSERT INTO ` logback_monitor `.` t_user ` (` id `, ` username `, ` password `, ` nickname `, ` last_login_time `, ` valid `, ` del `, ` locked `, ` public_key `, `
                                              private_key `, ` create_time `,
    ` create_by `, ` update_time `, ` update_by `)
VALUES (-1, 'by', '$2a$10$0fUQ4Nwm/yoa.vNiY/VZf.OcVzhXge3Ue4t9vWM4hGcFreL0D26n2', '超级管理员', '2023-02-25 11:34:44', 1, b '0', 0,
        'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWawhWbHO7iLgqx+SX51RRFsrgWGbh2vE9XJXYB9GvVSZ6e0KzK+LqADMyhraWQ6od5rcZMuwg0VRQ33jdORt5oyDtv7Hj9JRNgIdOwdCm7wMi6ORCRR0LqGdrf+6JxV7p8xM8veS8WEPUvZPuTmVmghva0TTY+AKD7Oma/wrRsQIDAQAB',
        'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANZrCFZsc7uIuCrH5JfnVFEWyuBYZuHa8T1cldgH0a9VJnp7QrMr4uoAMzKGtpZDqh3mtxky7CDRVFDfeN05G3mjIO2/seP0lE2Ah07B0KbvAyLo5EJFHQuoZ2t/7onFXunzEzy95LxYQ9S9k+5OZWaCG9rRNNj4AoPs6Zr/CtGxAgMBAAECgYADHz1Yls0rmJ7H3q+9J3zijmCnWhorZTb4hAHZiqmasjR0eZl7xuKPsi+Mp4n+j58pPswBNazbAHlIESzvGa8E9FBLtO/h6VXmQy/IZCEsQdjnZmVgvY4nzagCOCL2uL2iKKfoPTBSFiG/sDz5mDbR/Cd/iQ1l5rs9vY1TYMU3nQJBAOUlNbwTR5ygWfxL735xvOQ8WyXE6ihtzrk/OQFfpKnhDpH4rUksMXk+3T/e9Zl7FCfdAgO3BmdhkCy+ns5lJcUCQQDvi/o3GqkphngnmS4O+SKCHW7zRSJXVIhNcEMWnsLY/6lEyH8zHKs1+/XztBPRk4yB9Fmgo/8yoLEBirXgaGb9AkAqocaijC+5mzfCsdoH+186U2Rd3Yn3JZ7dZj7T25iGJqIQVGThBUDbHvAI7xFgkb6JQBwc1tNxLnylZn2jRrxFAkEAqZdSODz50lpPKsm54APluHSaHkStwWIpdZmrBZK0o+isr/O6ijcbx4ZwZG7zMQbjDQYvhhlVDUTSzCKmDRAdCQJAKlWJnGKNinAevcgwa4DfC9KzsJ9Y18dfXnzOLKIRzjlpAYsgkX+oBZIV5wsoktDclPE3JwHUNrDM07TBNVM/NQ==',
        '2023-02-17 20:17:47', 'system', '2023-02-25 19:34:44', 'admin2');
INSERT INTO ` logback_monitor `.` t_user_role ` (` user_id `, ` role_id `)
VALUES (-1, 3);