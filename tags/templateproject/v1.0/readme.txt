һ����Ŀ��ʼ������

1. Ԥ������
      ��templateprojectĿ¼����Ϊ��ĿСд����

2. �������У����л���ǰĿ¼����ĿĿ¼
   ����������ִ�С�init.bat ��ǰ׺ ��ĿСд���ơ�
      �� init.bat com.yourcompany demo
   Ϊ�˱����Ժ����ִ��init.bat��ִ����˲����ɾ��init.bat�ļ���
      
3. �������￪ʼ��������Eclpise�����в�����
      ʹ��Eclipse�Ĳ˵�File->Import��
    Ȼ��ѡ��General->Existing projects into workspace������Ŀ����Eclipse������

4. ��build.properties�ļ�
   �޸����ݿ�����

5. ����ִ��ant���񡾴�Eclipse��Ant��ͼ������Ŀ�е�build.xml��ק�����С�
   genconf (�������ݿ��Hibernate�����ļ�)
   genmapping (����Hibernateӳ���ļ�)
   genpojo(��genpojo2) (����Hibernateʵ�����ļ�)
   gencode(��gencode2) (���ɻ�������)
   
   ����ֱ��ִ�� genall(��genall2) ������в���
     �������������������annotation�����Ĵ��룬annotation�����Ĵ��벻��Ҫִ��genmapping��
   
��������ע������

1. applicationContext-service.xml�е�hibernate.show_sql��Ϊfalse��
2. ȥ��p6spy����sql�����ʹ���ˣ���
3. �����ȶ��󣬹رյ��ԡ�
