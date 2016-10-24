package scibd.lab.vms;

import java.util.Vector;

public class AllInfoData {

	private static Vector<InfoData> allInfoData = new Vector<InfoData>();

	/**
	 * @return the allState
	 */
	public static Vector<InfoData> getAllData() {
		return AllInfoData.allInfoData;
	}

	/**
	 *
	 */
	public static void setAllData(final Vector<InfoData> infoData1) {
		AllInfoData.allInfoData = infoData1;
	}

	/**
	 * @return the allState
	 */
	public static InfoData getData(final int pos) {
		return AllInfoData.allInfoData.get(pos);
	}

	/**
	 *
	 */
	public static void setData(final InfoData allInfoData1) {
		AllInfoData.allInfoData.addElement(allInfoData1);
	}

	public static void removeAllData() {

		AllInfoData.allInfoData.removeAllElements();
	}

	public static Vector<String> getAllDataBytitle() {
		final Vector<String> temp = new Vector<String>();
		temp.addElement("Select  ");

		for (final InfoData st : AllInfoData.allInfoData) {
			temp.addElement(st.getPage_title());
		}

		return temp;
	}

}
