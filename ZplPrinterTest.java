/*	private static boolean printPicking203DPI(ZplPrinter p) {
		// ��ߵ�����
		String bar1 = "07";
		p.setChar(bar1, 126, 86, 40, 40);
		String bar1Zpl = "^FO66,133^BY5,3.0,160^BCR,,N,N,N^FD${data}^FS";// ������ʽģ��
		p.setBarcode(bar1, bar1Zpl);
		// �±ߵ�����
		String bar2 = "00000999990018822969";// 20λ
		String bar2Paper = "^FO253,400^BY2,3.0,66^BCN,,Y,N,N^FD${data}^FS";// ������ʽģ��
		p.setBarcode(bar2, bar2Paper);
		p.setText("��ҩ�عɺ������޹�˾", 253, 26, 40, 40, 20, 1, 1, 24);
		p.setChar("CSS0BPKPPR", 253, 66, 20, 20);
		p.setText("09��", 626, 53, 40, 40, 20, 1, 1, 24);
		p.setText("��", 733, 53, 40, 40, 20, 1, 1, 24);
		p.setText("��˾������ ��·", 253, 120, 53, 53, 20, 1, 1, 24);
		p.setChar("03231151", 626, 124, 26, 26);
		p.setChar("2015-10-10", 626, 151, 20, 20);
		p.setText("�������ȴ�ҩ���������޹�˾", 253, 173, 40, 40, 20, 1, 1, 24);
		p.setText("��ɳ�п������̵����������258��", 253, 213, 30, 30, 20, 1, 1, 22);
		p.setText("��SKU", 533, 323, 40, 40, 20, 1, 1, 24);
		p.setText("��λ:49", 253, 280, 37, 37, 20, 1, 1, 24);
		p.setText("Ʒ��:��ð����", 253, 323, 37, 37, 20, 1, 1, 24);
		p.setText("����:", 253, 366, 37, 37, 20, 1, 1, 24);
		p.setChar("78787878788", 333, 373, 26, 26);
		String zpl = p.getZpl();
		System.out.println(zpl);
		boolean result = p.print(zpl);

		return result;

	}*/

/*	private static boolean printBarcode(ZplPrinter p) {
		// 1.��ӡ��������
		String bar0 = "131ZA010101";// ��������
		// String bar0Zpl =
		// "^FO110,110^BY6,3.0,280^BCN,,Y,N,N^FD${data}^FS";//������ʽģ��
		String bar0Zpl = "^FO80,350^AAN,100,110^BY4,2.0,200^BCN,,N,N,N^FD${data}^FS";// ������ʽģ��
		p.setBarcode(bar0, bar0Zpl);
		p.setChar(bar0, 100, 190, 140, 110);
		p.setText("��λ������", 880, 380, 60, 60, 30, 4, 4, 24);
		String zpl = p.getZpl();
		System.out.println(zpl);
		boolean result = p.print(zpl);// ��ӡ
		return result;
	}*/

		private static boolean printRestoking(ZplPrinter p, String bar1) {
		// �Ϸ���������
		String bar1Zpl = "^FO80,350^AAN,100,110^BY4,2.0,200^BCN,,N,N,N^FD${data}^FS";// ������ʽģ��
		p.setBarcode(bar1, bar1Zpl);
		p.setText(bar1, 80, 300, 30, 40, 30, 1, 1, 24);
		// Դ��λ
		p.setText("��Ʒ����:", 40, 70, 30, 30, 20, 1, 1, 24);
		// Ŀ���λ
		p.setText("��Ʒ���:", 500, 70, 30, 30, 20, 1, 1, 24);
		// ��Ʒ���
		p.setText("��Ʒ����:", 40, 150, 30, 30, 20, 1, 1, 24);
		// ��������
		p.setText("��������:"+DateUtils.getDate(), 500, 150, 30, 30, 10,1, 1, 24);
		// ��������
		p.setText("��������:", 40, 220, 30, 30, 20, 1, 1, 24);
		String zpl = p.getZpl();
		System.out.println(zpl);
		boolean result = p.print(zpl);
		return result;
	}
	