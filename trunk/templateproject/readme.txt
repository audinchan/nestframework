һ����Ŀ��ʼ������

1. ��templateprojectĿ¼����Ϊ��ĿСд���ơ�

2. ˫������init.bat�ļ���
	ִ����˲���ɾ��init.bat�ļ���
      
3. ʹ��Eclipse�Ĳ˵�File->Import��
	Ȼ��ѡ��General->Existing Projects into Workspace������Ŀ����Eclipse������

4. �༭build.properties�ļ�
	�޸����ݿ����ã�db.*��hibernate.dialect������Ҫ�޸ģ�

5. ��Eclipse��Ant��ͼ(Window->Show View->Ant)����build.xml��ק��Ant��ͼ�С�
	չ��Ant����ִ��genall���ɸ������úʹ���
	
	��������ִ������Ant���񡾿�ѡ��
		genconf (�������ݿ��Hibernate�����ļ�)
		genmapping (����Hibernateӳ���ļ�)
		genpojo (����Hibernateʵ�����ļ�)
		gencode (���ɻ�������)
		
������������ע������

1. ���ݿ�������Ψһ��������Զ��ϵ�м����⣩��
2. ��Զ��ϵ�м��ֻ������������ֶΣ��ֱ�ָ���ϵ���˱���������������ֶ�ͬʱ��Ϊ����������
3. Ĭ���Զ�����������ɻ��ƣ�����ָ�������޸�conf/hibernate.reveng.xml�ļ���
4. ����Ҫ�ų�ϵͳ�����޸�conf/hibernate.reveng.xml�ļ���
   
��������ע������

1. ��applicationContext-service.xml�е�hibernate.show_sql��Ϊfalse��
2. ȥ��p6spy����sql�����ʹ���ˣ���
3. �����ȶ��󣬹رյ��ԡ�
