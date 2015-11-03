/*
 * aTunes
 * Copyright (C) Alex Aranda, Sylvain Gaudard and contributors
 *
 * See http://www.atunes.org/wiki/index.php?title=Contributing for information about contributors
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.kernel.modules.playlist;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.atunes.model.IAudioObject;
import net.sourceforge.atunes.utils.StringUtils;

class PlaybackHistory {

    static class Heap {

        private List<IAudioObject> heap = new ArrayList<IAudioObject>();

        IAudioObject pop() {
            if (heap.isEmpty()) {
                return null;
            }
            IAudioObject ao = heap.get(heap.size() - 1);
            heap.remove(heap.size() - 1);
            return ao;
        }

        void push(IAudioObject ao) {
            heap.add(ao);
        }

        IAudioObject get(int index) {
            int position = heap.size() - index;
            if (!heap.isEmpty() && position >= 0 && position < heap.size()) {
                return heap.get(position);
            }
            return null;
        }

        void remove(IAudioObject ao) {
            // Remove all occurrences
            while (heap.contains(ao)) {
                heap.remove(ao);
            }
        }

        void clear() {
            heap.clear();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (IAudioObject ao : heap) {
                sb.append(ao).append(" <- ");
            }
            sb.append("]");
            return sb.toString();
        }

		void addToHistory(PlaybackHistory playbackHistory, IAudioObject audioObject) {
		    if (playbackHistory.currentAudioObject != null && !playbackHistory.currentAudioObject.equals(audioObject)) {
		        if (playbackHistory.previousHeap.get(1) == audioObject) {
		            playbackHistory.currentAudioObject = playbackHistory.previousHeap.pop();
		        } else if (get(1) == audioObject) {
		            playbackHistory.currentAudioObject = pop();
		        } else {
		            playbackHistory.previousHeap.push(playbackHistory.currentAudioObject);
		            playbackHistory.currentAudioObject = audioObject;
		        }
		    }
		}

    }

    private Heap previousHeap = new Heap();

    private IAudioObject currentAudioObject;

    Heap nextHeap = new Heap();

    IAudioObject getPreviousInHistory(int index) {
        return previousHeap.get(index);
    }

    IAudioObject getNextInHistory(int index) {
        return nextHeap.get(index);
    }

    IAudioObject moveToPreviousInHistory() {
        IAudioObject ao = previousHeap.pop();
        if (ao != null) {
            if (currentAudioObject != null) {
                nextHeap.push(currentAudioObject);
            }
            currentAudioObject = ao;
            return ao;
        }
        return null;
    }

    IAudioObject moveToNextInHistory() {
        IAudioObject ao = nextHeap.pop();
        if (ao != null) {
            if (currentAudioObject != null) {
                previousHeap.push(currentAudioObject);
                currentAudioObject = ao;
            }
            return ao;
        }
        return null;
    }

    void remove(List<IAudioObject> audioObjectsToRemove) {
        for (IAudioObject ao : audioObjectsToRemove) {
            previousHeap.remove(ao);
            nextHeap.remove(ao);
        }
    }

    void clear() {
        previousHeap.clear();
        nextHeap.clear();
        currentAudioObject = null;
    }

    @Override
    public String toString() {
        return StringUtils.getString(previousHeap.toString(), currentAudioObject, nextHeap.toString());
    }

}
