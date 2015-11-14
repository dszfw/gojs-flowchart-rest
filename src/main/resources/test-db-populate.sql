-- Sample data from http://gojs.net/latest/samples/flowchart.html
INSERT INTO processes (id, name) VALUES (100, 'go.GraphLinksModel');

INSERT INTO tasks (id, name, category, loc) VALUES (101, 'Kookie Brittle', 'Comment', '360 -10');
INSERT INTO tasks (id, name, category, loc) VALUES (102, 'Start', 'Start', '175 0');
INSERT INTO tasks (id, name, category, loc) VALUES (103, 'Preheat oven to 375 F', null, '0 77');
INSERT INTO tasks (id, name, category, loc) VALUES (104, 'In a bowl, blend: 1 cup margarine, 1.5 teaspoon vanilla, 1 teaspoon salt', null, '175 100');
INSERT INTO tasks (id, name, category, loc) VALUES (105, 'Gradually beat in 1 cup sugar and 2 cups sifted flour', NULL , '175 190');
INSERT INTO tasks (id, name, category, loc) VALUES (106, 'Mix in 6 oz (1 cup) Nestles Semi-Sweet Chocolate Morsels', NULL , '175 270');
INSERT INTO tasks (id, name, category, loc) VALUES (107, 'Press evenly into ungreased 15x10x1 pan', NULL , '175 370');
INSERT INTO tasks (id, name, category, loc) VALUES (108, 'Finely chop 1/2 cup of your choice of nuts', NULL , '352 85');
INSERT INTO tasks (id, name, category, loc) VALUES (109, '175 440', NULL , 'Sprinkle nuts on top');
INSERT INTO tasks (id, name, category, loc) VALUES (110, 'Bake for 25 minutes and let cool', NULL , '175 500');
INSERT INTO tasks (id, name, category, loc) VALUES (111, 'Cut into rectangular grid', NULL , '175 570');
INSERT INTO tasks (id, name, category, loc) VALUES (112, 'Enjoy!', 'End', '175 640');

INSERT INTO process_task (processId, taskId, position) VALUES (100, 101, -13);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 102, -1);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 103, 0);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 104, 1);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 105, 2);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 106, 3);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 107, 4);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 108, 5);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 109, 6);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 110, 7);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 111, 8);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 112, -2);

INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (101, 100, 104, 105, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (102, 100, 105, 106, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (103, 100, 106, 107, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (104, 100, 107, 109, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (105, 100, 109, 110, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (106, 100, 110, 111, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (107, 100, 111, 112, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (108, 100, 102, 103, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (109, 100, 102, 104, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (110, 100, 102, 108, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (111, 100, 108, 107, null, 'B', 'T');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (112, 100, 103, 107, null, 'B', 'T');

-- Integration tests data
INSERT INTO processes (id, name) VALUES (1000, 'Some Process');
INSERT INTO processes (id, name) VALUES (1001, 'Process#0');
INSERT INTO processes (id, name) VALUES (1002, 'Process#1');
INSERT INTO processes (id, name) VALUES (1003, 'Process#2');
INSERT INTO processes (id, name) VALUES (1004, 'Process#3');
INSERT INTO processes (id, name) VALUES (1005, 'Process#4');
INSERT INTO processes (id, name) VALUES (1006, 'Process#5');
INSERT INTO processes (id, name) VALUES (1007, 'Process#6');
INSERT INTO processes (id, name) VALUES (1008, 'Process#7');
INSERT INTO processes (id, name) VALUES (1009, 'Process#8');
INSERT INTO processes (id, name) VALUES (1010, 'Process#9');
INSERT INTO processes (id, name) VALUES (1011, 'Process that will be deleted');
INSERT INTO processes (id, name) VALUES (1012, 'Process that should be updated');

INSERT INTO tasks (id, name, category, loc) VALUES (1000, 'Some Task', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1001, 'Task#0', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1002, 'Task#1', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1003, 'Task#2', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1004, 'Task#3', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1005, 'Task#4', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1006, 'Task#5', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1007, 'Task#6', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1008, 'Task#7', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1009, 'Task#8', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1010, 'Task#9', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1011, 'Task that will be deleted', NULL , NULL );
INSERT INTO tasks (id, name, category, loc) VALUES (1012, 'Task that should be updated', NULL , NULL );

INSERT INTO process_task (processId, taskId, position) VALUES (1000, 1001, 0);
INSERT INTO process_task (processId, taskId, position) VALUES (1000, 1002, 0);
INSERT INTO process_task (processId, taskId, position) VALUES (1000, 1003, 0);
INSERT INTO process_task (processId, taskId, position) VALUES (1000, 1004, 0);

INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1000, 1000, 1001, 1002, 'Some TaskConnection', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1001, 1000, 1002, 1003, 'TaskConnection#0', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1002, 1000, 1004, 1002, 'TaskConnection#1', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1003, 1000, 1005, 1011, 'TaskConnection#2', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1004, 1000, 1006, 1005, 'TaskConnection#3', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1005, 1000, 1007, 1004, 'TaskConnection#4', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1006, 1000, 1008, 1003, 'TaskConnection#5', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1007, 1000, 1009, 1002, 'TaskConnection#6', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1008, 1000, 1010, 1009, 'TaskConnection#7', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1009, 1000, 1011, 1012, 'TaskConnection#8', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1010, 1000, 1012, 1010, 'TaskConnection#9', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1011, 1000, 1004, 1003, 'TaskConnection that will be deleted', 'A', 'B');
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (1012, 1000, 1003, 1002, 'TaskConnection that should be updated', 'A', 'B');
