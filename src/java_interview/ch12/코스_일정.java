package java_interview.ch12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 코스_일정 {
	/**
	 * DFS로 순환 구조 판별
	 * 순환 구조라면 계속 뱅글뱅글 맴돌게 될 것이고, 해당 코스를 처리할 수 없기 때문에 순한 판별 알고리즘을 구현해야 함
	 * Time Limit Exceeded로 타임아웃 발생 -> 비효율적 코드
	 */
	public boolean canFinish(int numCourses, int[][] prerequisites) {
		Map<Integer, List<Integer>> finishToTakeMap = new HashMap<>();
		// 완료하기 위해 처리해야 하는 일정을 finish -> take 형태의 그래프로 구성
		for (int[] pre : prerequisites) {
			// 값이 존재하지 않을 경우 빈 리스트 생성
			finishToTakeMap.putIfAbsent(pre[0], new ArrayList<>());
			// 처리해야 하는 코스 추가
			finishToTakeMap.get(pre[0]).add(pre[1]);
		}

		// 처리해야 하는 노드를 저장하는 변수
		List<Integer> takes = new ArrayList<>();
		// 완료해야 하는 노드 순회
		for (Integer finish : finishToTakeMap.keySet()) {
			// DFS 결과가 false라면 최종 결과도 false
			if (!dfs(finishToTakeMap, finish, takes)) {
				return false;
			}
		}
		// 모든 코스에 문제가 없으므로 true 리턴
		return true;
	}

	public boolean dfs(Map<Integer, List<Integer>> finishToTakeMap, Integer finish, List<Integer> takes) {
		// 완료해야 하는 노드가 처리해야 하는 노드에 이미 포함되어 있다면, 그래프가 순환 구조이므로 false 리턴
		if (takes.contains(finish)) {
			return false;
		}

		// 완료해야 하는 코스에 값이 있다면
		if (finishToTakeMap.containsKey(finish)) {
			// 처리해야 하는 노드에 현재 완료해야 하는 노드 추가
			takes.add(finish);
			// 처리해야 하는 노드 순회
			for (Integer take : finishToTakeMap.get(finish)) {
				// 재귀 DFS, 탐색 결과가 false라면 false를 리턴한다.
				if (!dfs(finishToTakeMap, take, takes)) {
					return false;
				}
			}
			// 탐색 후에 처리했으므로 노드 제거
			takes.remove(finish);
		}
		// 코스에 문제가 없으므로 true 리턴
		return true;
	}

	/**
	 * 가지치기를 이용한 최적화
	 * 한 번 방문했던 노드를 두 번 이상 방문하지 않도록 무조건 true를 리턴하도록 구조를 개선
	 */
	public boolean canFinish2(int numCourse, int[][] prerequisites) {
		Map<Integer, List<Integer>> finishToTakeMap = new HashMap<>();
		// 완료하기 위해 처리해야 하는 일정을 finish -> take 형태의 그래프로 구성
		for (int[] pre : prerequisites) {
			// 값이 존재하지 않을 경우 빈 리스트 생성
			finishToTakeMap.putIfAbsent(pre[0], new ArrayList<>());
			// 처리해야 하는 코스 추가
			finishToTakeMap.get(pre[0]).add(pre[1]);
		}

		// 처리해야 하는 노드를 저장하는 변수
		List<Integer> takes = new ArrayList<>();
		// 처리한 노드를 저장하는 변수
		List<Integer> taken = new ArrayList<>();
		// 완료해야 하는 노드 순회
		for (Integer finish : finishToTakeMap.keySet()) {
			// DFS 결과가 false라면 최종 결과도 false로 리턴
			if (!dfs2(finishToTakeMap, finish, takes, taken)) {
				return false;
			}
		}
		// 모든 코스에 문제가 없으므로 true 리턴
		return true;
	}

	private boolean dfs2(Map<Integer, List<Integer>> finishToTakeMap, Integer finish, List<Integer> takes,
		List<Integer> taken) {
		// 완료해야 하는 노드가 처리해야 하는 노드에 이미 포함되어 있다면, 그래프가 순환 구조이므로 false 리턴
		if (takes.contains(finish)) {
			return false;
		}

		// 이미 처리한 노드라면 true 리턴
		if (taken.contains(finish)) {
			return true;
		}

		// 완료해야 하는 코스에 값이 있다면
		if (finishToTakeMap.containsKey(finish)) {
			// 처리해야 하는 노드에 현재 완료해야 하는 노드 추가
			takes.add(finish);
			// 처리해야 하는 노드 순회
			for (Integer take : finishToTakeMap.get(finish)) {
				// 재귀 DFS, 탐색 결과가 false 라면 false를 리턴한다.
				if (!dfs2(finishToTakeMap, take, takes, taken)) {
					return false;
				}
			}
			// 탐색 후에는 처리했으므로 노드 제거
			takes.remove(finish);
			// 이미 처리한 노드 추가
			taken.add(finish);
		}
		// 코스에 문제가 없으므로 true 리턴
		return true;
	}
}
// https://leetcode.com/problems/course-schedule/description/