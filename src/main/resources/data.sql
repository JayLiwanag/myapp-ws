
INSERT INTO `role` VALUES (1, 'ROLE_ADMINISTRATOR'),
						  (2, 'ROLE_AUDITOR'),
                          (3, 'ROLE_USER');

INSERT INTO `privilege` VALUES (1, 'CREATE_NOTE'),
							   (2, 'EDIT_NOTE'),
							   (3, 'DELETE_NOTE'),
							   (4, 'VIEW_ALL_NOTE'),
							   (5, 'VIEW_NOTE');

/* ADMIN */
INSERT INTO `roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 1),
                               																  (1, 2),
                               																  (1, 3),
                               																  (1, 4),
                               																  (1, 5);

/* AUDITOR */
 INSERT INTO `roles_privileges` (`role_id`, `privilege_id`) VALUES (2, 4),
                               																  (2, 5);